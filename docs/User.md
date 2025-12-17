
# User

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **appUserId** | **kotlin.String** | The unique identifier of the user in your app. |  |
| **referralCode** | **kotlin.String** | The referral code of the user. |  |
| **referralCodeLink** | **kotlin.String** | The referral code link of the user. |  |
| **isTrial** | **kotlin.Boolean** | Whether the user is a trial user. |  |
| **isPremium** | **kotlin.Boolean** | Whether the user is a premium user. |  |
| **firstSeenAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) | The date when the user was first seen at. |  |
| **lastSeenAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) | The date when the user was last seen at. Deprecated, always returns null and will be removed in the future. |  |
| **metadata** | [**kotlin.Any**](.md) | The metadata of the user. |  |
| **stripeCustomerId** | **kotlin.String** | The unique identifier of the user in Stripe. |  |
| **claimCodeEligibility** | [**UserClaimCodeEligibility**](UserClaimCodeEligibility.md) | The claim code eligibility of the user. |  |
| **referredBy** | [**UserReferredBy**](UserReferredBy.md) | The referred by object of the user. |  |
| **stats** | [**UserStats**](UserStats.md) | The stats of the user. |  |
| **rewards** | [**UserRewards**](UserRewards.md) | The rewards of the user. |  |
| **referralProgram** | [**ReferralProgram**](ReferralProgram.md) | The program of the user. |  |



