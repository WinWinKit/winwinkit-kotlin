# RewardsActionsApi

All URIs are relative to *https://api.winwinkit.com*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**grantReward**](RewardsActionsApi.md#grantReward) | **POST** /users/{app_user_id}/rewards/grant | Grant a Reward |
| [**withdrawCredits**](RewardsActionsApi.md#withdrawCredits) | **POST** /users/{app_user_id}/rewards/withdraw-credits | Withdraw Credits |


<a id="grantReward"></a>
# **grantReward**
> UserGrantRewardResponse grantReward(appUserId, xApiKey, userGrantRewardRequest)

Grant a Reward

Grants a reward for a user. Note 1: currently only granting of credit rewards is supported. Note 2: this endpoint is only accessible with a secret API key.

### Example
```kotlin
// Import classes:
//import com.winwinkit.client.infrastructure.*
//import com.winwinkit.client.models.*

val apiInstance = RewardsActionsApi()
val appUserId : kotlin.String = appUserId_example // kotlin.String | The app user id of the user to grant a reward to.
val xApiKey : kotlin.String = xApiKey_example // kotlin.String | The secret API key.
val userGrantRewardRequest : UserGrantRewardRequest =  // UserGrantRewardRequest | 
try {
    val result : UserGrantRewardResponse = apiInstance.grantReward(appUserId, xApiKey, userGrantRewardRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling RewardsActionsApi#grantReward")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling RewardsActionsApi#grantReward")
    e.printStackTrace()
}
```

### Parameters
| **appUserId** | **kotlin.String**| The app user id of the user to grant a reward to. | |
| **xApiKey** | **kotlin.String**| The secret API key. | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userGrantRewardRequest** | [**UserGrantRewardRequest**](UserGrantRewardRequest.md)|  | |

### Return type

[**UserGrantRewardResponse**](UserGrantRewardResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="withdrawCredits"></a>
# **withdrawCredits**
> UserWithdrawCreditsResponse withdrawCredits(appUserId, xApiKey, userWithdrawCreditsRequest)

Withdraw Credits

Withdraws credits from a user.

### Example
```kotlin
// Import classes:
//import com.winwinkit.client.infrastructure.*
//import com.winwinkit.client.models.*

val apiInstance = RewardsActionsApi()
val appUserId : kotlin.String = appUserId_example // kotlin.String | The app user id of the user to withdraw credits from.
val xApiKey : kotlin.String = xApiKey_example // kotlin.String | The API key to authenticate with.
val userWithdrawCreditsRequest : UserWithdrawCreditsRequest =  // UserWithdrawCreditsRequest | 
try {
    val result : UserWithdrawCreditsResponse = apiInstance.withdrawCredits(appUserId, xApiKey, userWithdrawCreditsRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling RewardsActionsApi#withdrawCredits")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling RewardsActionsApi#withdrawCredits")
    e.printStackTrace()
}
```

### Parameters
| **appUserId** | **kotlin.String**| The app user id of the user to withdraw credits from. | |
| **xApiKey** | **kotlin.String**| The API key to authenticate with. | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userWithdrawCreditsRequest** | [**UserWithdrawCreditsRequest**](UserWithdrawCreditsRequest.md)|  | |

### Return type

[**UserWithdrawCreditsResponse**](UserWithdrawCreditsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

