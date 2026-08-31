# CodesApi

All URIs are relative to *https://api.winwinkit.com*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**getCode**](CodesApi.md#getCode) | **GET** /codes/{code} | Get Code |


<a id="getCode"></a>
# **getCode**
> CodeResponse getCode(code, xApiKey)

Get Code

Resolves a code to its type (affiliate, promo or referral) and returns the relevant data for that type. Requires no app user id.

### Example
```kotlin
// Import classes:
//import com.winwinkit.client.infrastructure.*
//import com.winwinkit.client.models.*

val apiInstance = CodesApi()
val code : kotlin.String = code_example // kotlin.String | The code to look up.
val xApiKey : kotlin.String = xApiKey_example // kotlin.String | The API key to authenticate with.
try {
    val result : CodeResponse = apiInstance.getCode(code, xApiKey)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CodesApi#getCode")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CodesApi#getCode")
    e.printStackTrace()
}
```

### Parameters
| **code** | **kotlin.String**| The code to look up. | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **xApiKey** | **kotlin.String**| The API key to authenticate with. | |

### Return type

[**CodeResponse**](CodeResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

