
appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%level %d{HH:mm:ss} %logger{15} [%thread] - %msg%n"
    }
}

root(INFO, ['STDOUT'])
logger('com.telecwin', DEBUG)
logger('org.grails', INFO)
// 打印 sql 语句
logger("org.hibernate.SQL", DEBUG, ["STDOUT"], false)
// 打印 sql 参数
logger("org.hibernate.type.descriptor.sql.BasicBinder", TRACE, ["STDOUT"], false)
