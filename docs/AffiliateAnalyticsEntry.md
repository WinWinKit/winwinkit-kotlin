
# AffiliateAnalyticsEntry

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.String** | The affiliate id |  |
| **name** | **kotlin.String** | The affiliate name |  |
| **acceptedAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) | When the affiliate joined the project |  |
| **status** | [**inline**](#Status) | The affiliate current standing with the project. Not always &#x60;approved&#x60;: an affiliate is reported for what they did in the period, so one since banned or deactivated still appears alongside the metrics they earned. |  |
| **totals** | [**AffiliateAnalyticsMetrics**](AffiliateAnalyticsMetrics.md) | The affiliate&#39;s totals over the period |  |


<a id="Status"></a>
## Enum: status
| Name | Value |
| ---- | ----- |
| status | pending, withdrawn, approved, rejected, archived, deactivated, banned |



