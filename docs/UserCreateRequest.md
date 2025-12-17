
# UserCreateRequest

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **appUserId** | **kotlin.String** | The unique identifier of the referral user in your app. |  |
| **isTrial** | **kotlin.Boolean** | Whether the user is a trial user. |  [optional] |
| **isPremium** | **kotlin.Boolean** | Whether the user is a premium user. |  [optional] |
| **firstSeenAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) | The date when the user was first seen at. |  [optional] |
| **lastSeenAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) | The date when the user was last seen at. Deprecated and will be removed in the future. |  [optional] |
| **metadata** | [**kotlin.Any**](.md) | The metadata of the user. |  [optional] |
| **stripeCustomerId** | **kotlin.String** | The unique identifier of the user in Stripe. |  [optional] |



