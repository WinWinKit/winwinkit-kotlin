# UsersApi

All URIs are relative to *https://api.winwinkit.com*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**createAffiliateApplyLink**](UsersApi.md#createAffiliateApplyLink) | **POST** /users/{app_user_id}/affiliate-apply-link | Create Affiliate Apply Link |
| [**createOrUpdateUser**](UsersApi.md#createOrUpdateUser) | **POST** /users | Create or Update User |
| [**getUser**](UsersApi.md#getUser) | **GET** /users/{app_user_id} | Get User |
| [**registerAppStoreTransaction**](UsersApi.md#registerAppStoreTransaction) | **POST** /users/{app_user_id}/transactions/app-store | Register App Store Transaction |
| [**registerGooglePlayTransaction**](UsersApi.md#registerGooglePlayTransaction) | **POST** /users/{app_user_id}/transactions/google-play | Register Google Play Transaction |


<a id="createAffiliateApplyLink"></a>
# **createAffiliateApplyLink**
> UserAffiliateApplyLinkResponse createAffiliateApplyLink(appUserId, xApiKey, userAffiliateApplyLinkRequest)

Create Affiliate Apply Link

Builds an affiliate apply link for the user, carrying a short-lived token so the affiliate they become is attributed back to them. Request it when the user asks to apply, rather than ahead of time.

### Example
```kotlin
// Import classes:
//import com.winwinkit.client.infrastructure.*
//import com.winwinkit.client.models.*

val apiInstance = UsersApi()
val appUserId : kotlin.String = appUserId_example // kotlin.String | The app user id of the user applying.
val xApiKey : kotlin.String = xApiKey_example // kotlin.String | The API key to authenticate with.
val userAffiliateApplyLinkRequest : UserAffiliateApplyLinkRequest =  // UserAffiliateApplyLinkRequest | 
try {
    val result : UserAffiliateApplyLinkResponse = apiInstance.createAffiliateApplyLink(appUserId, xApiKey, userAffiliateApplyLinkRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#createAffiliateApplyLink")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#createAffiliateApplyLink")
    e.printStackTrace()
}
```

### Parameters
| **appUserId** | **kotlin.String**| The app user id of the user applying. | |
| **xApiKey** | **kotlin.String**| The API key to authenticate with. | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userAffiliateApplyLinkRequest** | [**UserAffiliateApplyLinkRequest**](UserAffiliateApplyLinkRequest.md)|  | |

### Return type

[**UserAffiliateApplyLinkResponse**](UserAffiliateApplyLinkResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="createOrUpdateUser"></a>
# **createOrUpdateUser**
> UserResponse createOrUpdateUser(xApiKey, userCreateRequest)

Create or Update User

Create or update a user if already exists.

### Example
```kotlin
// Import classes:
//import com.winwinkit.client.infrastructure.*
//import com.winwinkit.client.models.*

val apiInstance = UsersApi()
val xApiKey : kotlin.String = xApiKey_example // kotlin.String | The API key to authenticate with.
val userCreateRequest : UserCreateRequest =  // UserCreateRequest | 
try {
    val result : UserResponse = apiInstance.createOrUpdateUser(xApiKey, userCreateRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#createOrUpdateUser")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#createOrUpdateUser")
    e.printStackTrace()
}
```

### Parameters
| **xApiKey** | **kotlin.String**| The API key to authenticate with. | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userCreateRequest** | [**UserCreateRequest**](UserCreateRequest.md)|  | |

### Return type

[**UserResponse**](UserResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="getUser"></a>
# **getUser**
> UserResponse getUser(appUserId, xApiKey)

Get User

Retrieves a user by their app user id.

### Example
```kotlin
// Import classes:
//import com.winwinkit.client.infrastructure.*
//import com.winwinkit.client.models.*

val apiInstance = UsersApi()
val appUserId : kotlin.String = appUserId_example // kotlin.String | The app user id of the user to retrieve.
val xApiKey : kotlin.String = xApiKey_example // kotlin.String | The API key to authenticate with.
try {
    val result : UserResponse = apiInstance.getUser(appUserId, xApiKey)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#getUser")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#getUser")
    e.printStackTrace()
}
```

### Parameters
| **appUserId** | **kotlin.String**| The app user id of the user to retrieve. | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **xApiKey** | **kotlin.String**| The API key to authenticate with. | |

### Return type

[**UserResponse**](UserResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="registerAppStoreTransaction"></a>
# **registerAppStoreTransaction**
> registerAppStoreTransaction(appUserId, xApiKey, userRegisterAppStoreTransactionRequest)

Register App Store Transaction

Registers the mapping between a user and their Apple originalTransactionId.

### Example
```kotlin
// Import classes:
//import com.winwinkit.client.infrastructure.*
//import com.winwinkit.client.models.*

val apiInstance = UsersApi()
val appUserId : kotlin.String = appUserId_example // kotlin.String | The app user id of the user.
val xApiKey : kotlin.String = xApiKey_example // kotlin.String | The API key to authenticate with.
val userRegisterAppStoreTransactionRequest : UserRegisterAppStoreTransactionRequest =  // UserRegisterAppStoreTransactionRequest | 
try {
    apiInstance.registerAppStoreTransaction(appUserId, xApiKey, userRegisterAppStoreTransactionRequest)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#registerAppStoreTransaction")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#registerAppStoreTransaction")
    e.printStackTrace()
}
```

### Parameters
| **appUserId** | **kotlin.String**| The app user id of the user. | |
| **xApiKey** | **kotlin.String**| The API key to authenticate with. | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userRegisterAppStoreTransactionRequest** | [**UserRegisterAppStoreTransactionRequest**](UserRegisterAppStoreTransactionRequest.md)|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="registerGooglePlayTransaction"></a>
# **registerGooglePlayTransaction**
> registerGooglePlayTransaction(appUserId, xApiKey, userRegisterGooglePlayTransactionRequest)

Register Google Play Transaction

Registers the mapping between a user and their Google Play purchaseToken.

### Example
```kotlin
// Import classes:
//import com.winwinkit.client.infrastructure.*
//import com.winwinkit.client.models.*

val apiInstance = UsersApi()
val appUserId : kotlin.String = appUserId_example // kotlin.String | The app user id of the user.
val xApiKey : kotlin.String = xApiKey_example // kotlin.String | The API key to authenticate with.
val userRegisterGooglePlayTransactionRequest : UserRegisterGooglePlayTransactionRequest =  // UserRegisterGooglePlayTransactionRequest | 
try {
    apiInstance.registerGooglePlayTransaction(appUserId, xApiKey, userRegisterGooglePlayTransactionRequest)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#registerGooglePlayTransaction")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#registerGooglePlayTransaction")
    e.printStackTrace()
}
```

### Parameters
| **appUserId** | **kotlin.String**| The app user id of the user. | |
| **xApiKey** | **kotlin.String**| The API key to authenticate with. | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userRegisterGooglePlayTransactionRequest** | [**UserRegisterGooglePlayTransactionRequest**](UserRegisterGooglePlayTransactionRequest.md)|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

