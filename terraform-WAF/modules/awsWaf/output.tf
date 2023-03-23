output "waf_log_group_arn" {
  value = aws_cloudwatch_log_group.wafv2-log-group.arn
}