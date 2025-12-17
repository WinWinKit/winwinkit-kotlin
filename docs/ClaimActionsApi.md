# ClaimActionsApi

All URIs are relative to *https://api.winwinkit.com*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**claimCode**](ClaimActionsApi.md#claimCode) | **POST** /users/{app_user_id}/claim-code | Claim Code |


<a id="claimCode"></a>
# **claimCode**
> UserClaimCodeResponse claimCode(appUserId, xApiKey, userClaimCodeRequest)

Claim Code

Claims a code for a user. Code can be affiliate, promo or referral code.

### Example
```kotlin
// Import classes:
//import com.winwinkit.client.infrastructure.*
//import com.winwinkit.client.models.*

val apiInstance = ClaimActionsApi()
val appUserId : kotlin.String = appUserId_example // kotlin.String | The app user id of the user to claim the code for.
val xApiKey : kotlin.String = xApiKey_example // kotlin.String | The API key to authenticate with.
val userClaimCodeRequest : UserClaimCodeRequest =  // UserClaimCodeRequest | 
try {
    val result : UserClaimCodeResponse = apiInstance.claimCode(appUserId, xApiKey, userClaimCodeRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ClaimActionsApi#claimCode")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ClaimActionsApi#claimCode")
    e.printStackTrace()
}
```

### Parameters
| **appUserId** | **kotlin.String**| The app user id of the user to claim the code for. | |
| **xApiKey** | **kotlin.String**| The API key to authenticate with. | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userClaimCodeRequest** | [**UserClaimCodeRequest**](UserClaimCodeRequest.md)|  | |

### Return type

[**UserClaimCodeResponse**](UserClaimCodeResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

