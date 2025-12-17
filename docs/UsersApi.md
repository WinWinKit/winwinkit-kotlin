# UsersApi

All URIs are relative to *https://api.winwinkit.com*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**createOrUpdateUser**](UsersApi.md#createOrUpdateUser) | **POST** /users | Create or Update User |
| [**getUser**](UsersApi.md#getUser) | **GET** /users/{app_user_id} | Get User |


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

