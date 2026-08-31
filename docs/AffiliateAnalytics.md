
# AffiliateAnalytics

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **from** | **kotlin.String** | The first day of the period, inclusive |  |
| **to** | **kotlin.String** | The last day of the period, inclusive |  |
| **totals** | [**AffiliateAnalyticsMetrics**](AffiliateAnalyticsMetrics.md) | The totals across every affiliate in the period, not just this page |  |
| **affiliates** | [**kotlin.collections.List&lt;AffiliateAnalyticsEntry&gt;**](AffiliateAnalyticsEntry.md) | The page of affiliates, oldest joiner first. Affiliates with no activity in the period are omitted. |  |
| **pagination** | [**Pagination**](Pagination.md) | Where this page sits within the affiliates that matched |  |



