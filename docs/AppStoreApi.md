# AppStoreApi

All URIs are relative to *https://api.winwinkit.com*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**getOfferCode**](AppStoreApi.md#getOfferCode) | **GET** /app-store/offer-codes/{offer_code_id} | Get Offer Code |


<a id="getOfferCode"></a>
# **getOfferCode**
> OfferCodeResponse getOfferCode(offerCodeId, xApiKey)

Get Offer Code

Get an offer code with subscription and prices by the offer code id.

### Example
```kotlin
// Import classes:
//import com.winwinkit.client.infrastructure.*
//import com.winwinkit.client.models.*

val apiInstance = AppStoreApi()
val offerCodeId : kotlin.String = offerCodeId_example // kotlin.String | The offer code id to retrieve.
val xApiKey : kotlin.String = xApiKey_example // kotlin.String | The API key to authenticate with.
try {
    val result : OfferCodeResponse = apiInstance.getOfferCode(offerCodeId, xApiKey)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling AppStoreApi#getOfferCode")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AppStoreApi#getOfferCode")
    e.printStackTrace()
}
```

### Parameters
| **offerCodeId** | **kotlin.String**| The offer code id to retrieve. | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **xApiKey** | **kotlin.String**| The API key to authenticate with. | |

### Return type

[**OfferCodeResponse**](OfferCodeResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

