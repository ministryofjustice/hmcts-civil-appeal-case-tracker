variable "aws_lb_name" {
  description = "The name of the application load balancer"
}

variable "aws_wafv2_web_acl_name" {
  description = "The name of the web access List in Web Application Firewall"
}

variable "aws_wafv2_web_acl_description" {
  description = "The description of the web acl"
}

variable "aws_cloudwatch_log_group_name" {
  description = "The name of the log group in cloudwatch"
}
