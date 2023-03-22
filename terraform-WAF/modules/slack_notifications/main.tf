locals {
  function_name = "${var.service_name}_${var.environment}_send_waf_slack_notification"
}

resource "aws_lambda_function" "slack_notification" {
  filename      = "slack_lambda.py.zip"
  function_name = local.function_name
  role          = aws_iam_role.lambda_exec.arn
  handler       = "slack_lambda.lambda_handler"
  runtime       = "python3.9"
  timeout       = 60

  environment {
    variables = {
      SLACK_WEBHOOK_URL = var.slack_webhook_url
    }
  }
  depends_on = [
    aws_iam_role_policy_attachment.lambda_exec_policy_attachment,
    aws_cloudwatch_log_group.log_group
  ]
}

resource "aws_cloudwatch_log_group" "log_group" {
  name              = "/aws/lambda/${local.function_name}"
  retention_in_days = 14
}

resource "aws_cloudwatch_log_subscription_filter" "lambda_trigger" {
  depends_on = [
    aws_lambda_permission.allow_cloudwatch_logs
  ]
  name           = "waf_lambda_trigger"
  log_group_name = var.aws_cloudwatch_log_group_name
  filter_pattern = "{ $.httpRequest.clientIp = 82.8.229.204 || $.httpRequest.clientIp = 81.134.202.29 }" //Example - create slack alerts for log entries matching this filter

  destination_arn = aws_lambda_function.slack_notification.arn
}

resource "aws_iam_role" "lambda_exec" {
  name = "lambda_exec_${var.service_name}_${var.environment}"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "lambda.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "lambda_exec_policy_attachment" {
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
  role       = aws_iam_role.lambda_exec.name
}

resource "aws_lambda_permission" "allow_cloudwatch_logs" {
  statement_id  = "AllowExecutionFromCloudWatchLogs"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.slack_notification.function_name
  principal     = "logs.amazonaws.com"
  source_arn    = "${var.waf_log_group_arn}:*"
}
