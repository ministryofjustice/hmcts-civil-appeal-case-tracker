variable "slack_webhook_url" {
  type        = string
  description = "The URL for the Slack webhook to send notifications to"
}

variable "aws_cloudwatch_log_group_name" {
  description = "The name of the log group in cloudwatch"
}

variable "waf_log_group_arn" {
  description = "The ARN of the WAF Cloudwatch log group"
}

variable "environment" {
  type        = string
  description = "Environment name e.g. dev, staging or prod"
}

variable "service_name" {
  type        = string
  description = "service name"
}
