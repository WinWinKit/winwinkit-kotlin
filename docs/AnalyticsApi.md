# AnalyticsApi

All URIs are relative to *https://api.winwinkit.com*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**getAffiliateAnalytics**](AnalyticsApi.md#getAffiliateAnalytics) | **GET** /analytics/affiliates | Get Affiliate Analytics |


<a id="getAffiliateAnalytics"></a>
# **getAffiliateAnalytics**
> AffiliateAnalyticsResponse getAffiliateAnalytics(from, to, xApiKey, limit, offset)

Get Affiliate Analytics

Returns the project affiliates with their totals for a period, oldest joiner first. Affiliates with no activity in the period are omitted. Note 1: this endpoint is only accessible with a secret API key. Note 2: a period may span at most 90 days; request a longer window a period at a time. Note 3: up to 100 affiliates are returned per request; page through the rest with &#x60;offset&#x60;.

### Example
```kotlin
// Import classes:
//import com.winwinkit.client.infrastructure.*
//import com.winwinkit.client.models.*

val apiInstance = AnalyticsApi()
val from : java.time.LocalDate = Thu Jan 01 01:00:00 CET 2026 // java.time.LocalDate | The first day of the period, inclusive, as YYYY-MM-DD (UTC).
val to : java.time.LocalDate = Tue Mar 31 02:00:00 CEST 2026 // java.time.LocalDate | The last day of the period, inclusive, as YYYY-MM-DD (UTC). Must not be before `from`, and the period must be at most 90 days long.
val xApiKey : kotlin.String = xApiKey_example // kotlin.String | The secret API key.
val limit : java.math.BigDecimal = 100 // java.math.BigDecimal | How many affiliates to return, from 10 to 100.
val offset : java.math.BigDecimal = 0 // java.math.BigDecimal | How many affiliates to skip before the page.
try {
    val result : AffiliateAnalyticsResponse = apiInstance.getAffiliateAnalytics(from, to, xApiKey, limit, offset)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling AnalyticsApi#getAffiliateAnalytics")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AnalyticsApi#getAffiliateAnalytics")
    e.printStackTrace()
}
```

### Parameters
| **from** | **java.time.LocalDate**| The first day of the period, inclusive, as YYYY-MM-DD (UTC). | |
| **to** | **java.time.LocalDate**| The last day of the period, inclusive, as YYYY-MM-DD (UTC). Must not be before &#x60;from&#x60;, and the period must be at most 90 days long. | |
| **xApiKey** | **kotlin.String**| The secret API key. | |
| **limit** | **java.math.BigDecimal**| How many affiliates to return, from 10 to 100. | [optional] [default to 100] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **offset** | **java.math.BigDecimal**| How many affiliates to skip before the page. | [optional] [default to 0] |

### Return type

[**AffiliateAnalyticsResponse**](AffiliateAnalyticsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

