#!/bin/bash

# ================================================
# Jar应用管理脚本
# 功能：启动、停止、重启、状态检查、日志查看等
# ================================================

# 配置区域 - 根据实际情况修改
APP_NAME="demo"          # 应用名称
JAR_PATH="/opt/demo.jar"          # Jar包完整路径
JAVA_OPTS="-Xms512m -Xmx1024m"     # JVM参数
SPRING_OPTS="--spring.profiles.active=prod"  # Spring参数
LOG_DIR="/opt/logs"               # 日志目录
PID_FILE="/opt/${APP_NAME}.pid"   # PID文件路径
BACKUP_DIR="/opt/backups"         # 备份目录
WAIT_TIMEOUT=30                   # 等待停止超时时间(秒)

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印彩色信息
print_info() { echo -e "${BLUE}[INFO]${NC} $1"; }
print_success() { echo -e "${GREEN}[SUCCESS]${NC} $1"; }
print_warning() { echo -e "${YELLOW}[WARNING]${NC} $1"; }
print_error() { echo -e "${RED}[ERROR]${NC} $1"; }

# 检查必要目录
check_directories() {
    local dirs=("$(dirname "$JAR_PATH")" "$LOG_DIR" "$BACKUP_DIR")
    for dir in "${dirs[@]}"; do
        if [ ! -d "$dir" ]; then
            print_warning "目录不存在: $dir，正在创建..."
            mkdir -p "$dir"
        fi
    done
}

# 获取进程PID
get_pid() {
    if [ -f "$PID_FILE" ]; then
        local pid=$(cat "$PID_FILE")
        if ps -p "$pid" > /dev/null 2>&1; then
            echo "$pid"
            return 0
        else
            rm -f "$PID_FILE"
        fi
    fi
    echo ""
}

# 启动应用
start() {
    print_info "正在启动 $APP_NAME..."
    
    local pid=$(get_pid)
    if [ -n "$pid" ]; then
        print_warning "$APP_NAME 已经在运行 (PID: $pid)"
        return 1
    fi

    # 检查Jar文件是否存在
    if [ ! -f "$JAR_PATH" ]; then
        print_error "Jar文件不存在: $JAR_PATH"
        return 1
    fi

    # 创建日志文件
    local log_file="$LOG_DIR/${APP_NAME}.log"
    local current_time=$(date '+%Y-%m-%d %H:%M:%S')
    
    echo "=== 应用启动时间: $current_time ===" >> "$log_file"
    
    # 启动应用
    nohup java $JAVA_OPTS -jar "$JAR_PATH" $SPRING_OPTS >> "$log_file" 2>&1 &
    local new_pid=$!
    
    # 保存PID
    echo $new_pid > "$PID_FILE"
    
    print_info "应用启动中，PID: $new_pid"
    print_info "日志文件: $log_file"
    
    # 等待并检查启动是否成功
    sleep 5
    if ps -p $new_pid > /dev/null 2>&1; then
        print_success "$APP_NAME 启动成功 (PID: $new_pid)"
        return 0
    else
        print_error "$APP_NAME 启动失败，请检查日志: $log_file"
        rm -f "$PID_FILE"
        return 1
    fi
}

# 停止应用
stop() {
    print_info "正在停止 $APP_NAME..."
    
    local pid=$(get_pid)
    if [ -z "$pid" ]; then
        print_warning "$APP_NAME 未在运行"
        return 0
    fi

    # 发送停止信号
    kill $pid
    local count=0
    
    # 等待进程停止
    while [ $count -lt $WAIT_TIMEOUT ]; do
        if ps -p $pid > /dev/null 2>&1; then
            sleep 1
            count=$((count + 1))
        else
            break
        fi
    done

    # 强制停止（如果正常停止失败）
    if ps -p $pid > /dev/null 2>&1; then
        print_warning "正常停止超时，尝试强制停止..."
        kill -9 $pid
        sleep 2
    fi

    # 清理PID文件
    if [ ! -f "$PID_FILE" ] || [ -z "$(get_pid)" ]; then
        rm -f "$PID_FILE"
        print_success "$APP_NAME 已停止"
        return 0
    else
        print_error "停止 $APP_NAME 失败"
        return 1
    fi
}

# 重启应用
restart() {
    stop
    if [ $? -eq 0 ]; then
        sleep 2
        start
    else
        print_error "停止失败，无法重启"
        return 1
    fi
}

# 检查应用状态
status() {
    local pid=$(get_pid)
    if [ -n "$pid" ]; then
        print_success "$APP_NAME 正在运行 (PID: $pid)"
        # 显示进程详细信息
        ps -fp $pid
        # 显示内存使用情况
        local mem_info=$(ps -o rss= -p $pid 2>/dev/null)
        if [ -n "$mem_info" ]; then
            local mem_mb=$((mem_info / 1024))
            print_info "内存使用: ${mem_mb}MB"
        fi
        return 0
    else
        print_error "$APP_NAME 未在运行"
        return 1
    fi
}

# 查看日志
log() {
    local log_file="$LOG_DIR/${APP_NAME}.log"
    if [ ! -f "$log_file" ]; then
        print_error "日志文件不存在: $log_file"
        return 1
    fi

    case "$1" in
        "tail")
            print_info "实时查看日志 (Ctrl+C 退出)..."
            tail -f "$log_file"
            ;;
        "error")
            print_info "查看错误日志:"
            grep -i "error\|exception\|failed" "$log_file" | tail -50
            ;;
        "today")
            local today=$(date '+%Y-%m-%d')
            print_info "今日日志:"
            grep "^\[$today" "$log_file" || grep "$today" "$log_file"
            ;;
        *)
            print_info "查看最新日志:"
            tail -100 "$log_file"
            ;;
    esac
}

# 备份应用
backup() {
    print_info "正在备份应用..."
    
    if [ ! -f "$JAR_PATH" ]; then
        print_error "Jar文件不存在，无法备份"
        return 1
    fi

    local backup_name="${APP_NAME}_backup_$(date '+%Y%m%d_%H%M%S').jar"
    local backup_path="$BACKUP_DIR/$backup_name"
    
    cp "$JAR_PATH" "$backup_path"
    
    if [ $? -eq 0 ]; then
        print_success "备份完成: $backup_path"
        # 清理旧备份，只保留最近5个
        ls -t "$BACKUP_DIR"/*.jar 2>/dev/null | tail -n +6 | xargs rm -f 2>/dev/null
    else
        print_error "备份失败"
        return 1
    fi
}

# 监控应用
monitor() {
    print_info "开始监控 $APP_NAME..."
    
    while true; do
        local pid=$(get_pid)
        if [ -z "$pid" ]; then
            print_warning "应用未运行，尝试重新启动..."
            start
        else
            # 检查应用是否响应（简单版健康检查）
            # 这里可以根据实际情况添加更复杂的健康检查逻辑
            print_info "应用运行正常 (PID: $pid) - $(date '+%Y-%m-%d %H:%M:%S')"
        fi
        sleep 60  # 每分钟检查一次
    done
}

# 显示使用帮助
usage() {
    echo "用法: $0 {start|stop|restart|status|log|backup|monitor|help}"
    echo ""
    echo "命令说明:"
    echo "  start    启动应用"
    echo "  stop     停止应用"
    echo "  restart  重启应用"
    echo "  status   查看应用状态"
    echo "  log      查看日志 [tail|error|today]"
    echo "  backup   备份应用"
    echo "  monitor  监控并自动重启应用"
    echo "  help     显示此帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 start           # 启动应用"
    echo "  $0 log tail        # 实时查看日志"
    echo "  $0 log error       # 查看错误日志"
}

# 主函数
main() {
    # 检查必要目录
    check_directories
    
    case "$1" in
        "start")
            start
            ;;
        "stop")
            stop
            ;;
        "restart")
            restart
            ;;
        "status")
            status
            ;;
        "log")
            log "$2"
            ;;
        "backup")
            backup
            ;;
        "monitor")
            monitor
            ;;
        "help"|"")
            usage
            ;;
        *)
            print_error "未知命令: $1"
            usage
            exit 1
            ;;
    esac
}

# 脚本入口
if [ "${BASH_SOURCE[0]}" = "$0" ]; then
    main "$@"
fi