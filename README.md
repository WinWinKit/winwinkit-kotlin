# Affiliates & Referrals for Android apps

The official [WinWinKit](https://winwinkit.com) SDK for Android and Kotlin Multiplatform.

## Getting Started

Please follow the [Kotlin SDK](https://winwinkit.com/docs/sdk/kotlin-sdk) guide for information on how to install the SDK.

Additionally, check out our [docs](https://winwinkit.com/docs) and [guides](https://winwinkit.com/guides) for more information about the platform.

## Requirements

* Kotlin 1.7.21
* Gradle 7.5

## Features/Implementation Notes

* Supports JSON inputs/outputs, File inputs, and Form inputs.
* Supports collection formats for query parameters: csv, tsv, ssv, pipes.
* Some Kotlin and Java types are fully qualified to avoid conflicts with types defined in OpenAPI definitions.
* Implementation of ApiClient is intended to reduce method counts, specifically to benefit Android targets.

<a id="documentation-for-api-endpoints"></a>
## Documentation for API Endpoints

All URIs are relative to *https://api.winwinkit.com*

| Class | Method | HTTP request | Description |
| ------------ | ------------- | ------------- | ------------- |
| *AppStoreApi* | [**getOfferCode**](docs/AppStoreApi.md#getoffercode) | **GET** /app-store/offer-codes/{offer_code_id} | Get Offer Code |
| *ClaimActionsApi* | [**claimCode**](docs/ClaimActionsApi.md#claimcode) | **POST** /users/{app_user_id}/claim-code | Claim Code |
| *RewardsActionsApi* | [**grantReward**](docs/RewardsActionsApi.md#grantreward) | **POST** /users/{app_user_id}/rewards/grant | Grant a Reward |
| *RewardsActionsApi* | [**withdrawCredits**](docs/RewardsActionsApi.md#withdrawcredits) | **POST** /users/{app_user_id}/rewards/withdraw-credits | Withdraw Credits |
| *UsersApi* | [**createOrUpdateUser**](docs/UsersApi.md#createorupdateuser) | **POST** /users | Create or Update User |
| *UsersApi* | [**getUser**](docs/UsersApi.md#getuser) | **GET** /users/{app_user_id} | Get User |


<a id="documentation-for-models"></a>
## Documentation for Models

 - [com.winwinkit.client.models.AppStoreOfferCode](docs/AppStoreOfferCode.md)
 - [com.winwinkit.client.models.AppStorePrice](docs/AppStorePrice.md)
 - [com.winwinkit.client.models.AppStoreSubscription](docs/AppStoreSubscription.md)
 - [com.winwinkit.client.models.BasicReward](docs/BasicReward.md)
 - [com.winwinkit.client.models.CreditReward](docs/CreditReward.md)
 - [com.winwinkit.client.models.ErrorObject](docs/ErrorObject.md)
 - [com.winwinkit.client.models.ErrorsResponse](docs/ErrorsResponse.md)
 - [com.winwinkit.client.models.GooglePlayPromoCodeReward](docs/GooglePlayPromoCodeReward.md)
 - [com.winwinkit.client.models.GooglePlayPromoCodeValue](docs/GooglePlayPromoCodeValue.md)
 - [com.winwinkit.client.models.OfferCodeResponse](docs/OfferCodeResponse.md)
 - [com.winwinkit.client.models.OfferCodeResponseData](docs/OfferCodeResponseData.md)
 - [com.winwinkit.client.models.OfferCodeReward](docs/OfferCodeReward.md)
 - [com.winwinkit.client.models.OfferCodeValue](docs/OfferCodeValue.md)
 - [com.winwinkit.client.models.ReferralProgram](docs/ReferralProgram.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverBasicReward](docs/ReferralProgramReceiverBasicReward.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverBasicRewardActivation](docs/ReferralProgramReceiverBasicRewardActivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverBasicRewardDeactivation](docs/ReferralProgramReceiverBasicRewardDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverBasicRewardIntervalDeactivation](docs/ReferralProgramReceiverBasicRewardIntervalDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverBasicRewardNeverDeactivation](docs/ReferralProgramReceiverBasicRewardNeverDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverCreditReward](docs/ReferralProgramReceiverCreditReward.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverCreditRewardActivation](docs/ReferralProgramReceiverCreditRewardActivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverCreditRewardDeactivation](docs/ReferralProgramReceiverCreditRewardDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverCreditRewardIntervalDeactivation](docs/ReferralProgramReceiverCreditRewardIntervalDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverCreditRewardNeverDeactivation](docs/ReferralProgramReceiverCreditRewardNeverDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverGooglePlayPromoCodeReward](docs/ReferralProgramReceiverGooglePlayPromoCodeReward.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverGooglePlayPromoCodeRewardActivation](docs/ReferralProgramReceiverGooglePlayPromoCodeRewardActivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverGooglePlayPromoCodeRewardDeactivation](docs/ReferralProgramReceiverGooglePlayPromoCodeRewardDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverGooglePlayPromoCodeRewardIntervalDeactivation](docs/ReferralProgramReceiverGooglePlayPromoCodeRewardIntervalDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverGooglePlayPromoCodeRewardNeverDeactivation](docs/ReferralProgramReceiverGooglePlayPromoCodeRewardNeverDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverOfferCodeReward](docs/ReferralProgramReceiverOfferCodeReward.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverOfferCodeRewardActivation](docs/ReferralProgramReceiverOfferCodeRewardActivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverOfferCodeRewardDeactivation](docs/ReferralProgramReceiverOfferCodeRewardDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverOfferCodeRewardIntervalDeactivation](docs/ReferralProgramReceiverOfferCodeRewardIntervalDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverOfferCodeRewardNeverDeactivation](docs/ReferralProgramReceiverOfferCodeRewardNeverDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverRevenueCatEntitlementReward](docs/ReferralProgramReceiverRevenueCatEntitlementReward.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverRevenueCatEntitlementRewardActivation](docs/ReferralProgramReceiverRevenueCatEntitlementRewardActivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverRevenueCatEntitlementRewardDeactivation](docs/ReferralProgramReceiverRevenueCatEntitlementRewardDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverRevenueCatEntitlementRewardIntervalDeactivation](docs/ReferralProgramReceiverRevenueCatEntitlementRewardIntervalDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverRevenueCatEntitlementRewardNeverDeactivation](docs/ReferralProgramReceiverRevenueCatEntitlementRewardNeverDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverRevenueCatOfferingReward](docs/ReferralProgramReceiverRevenueCatOfferingReward.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverRevenueCatOfferingRewardActivation](docs/ReferralProgramReceiverRevenueCatOfferingRewardActivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverRevenueCatOfferingRewardDeactivation](docs/ReferralProgramReceiverRevenueCatOfferingRewardDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverRevenueCatOfferingRewardIntervalDeactivation](docs/ReferralProgramReceiverRevenueCatOfferingRewardIntervalDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverRevenueCatOfferingRewardNeverDeactivation](docs/ReferralProgramReceiverRevenueCatOfferingRewardNeverDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramReceiverRewards](docs/ReferralProgramReceiverRewards.md)
 - [com.winwinkit.client.models.ReferralProgramRewards](docs/ReferralProgramRewards.md)
 - [com.winwinkit.client.models.ReferralProgramSenderBasicReward](docs/ReferralProgramSenderBasicReward.md)
 - [com.winwinkit.client.models.ReferralProgramSenderBasicRewardActivation](docs/ReferralProgramSenderBasicRewardActivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderBasicRewardDeactivation](docs/ReferralProgramSenderBasicRewardDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderBasicRewardIntervalDeactivation](docs/ReferralProgramSenderBasicRewardIntervalDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderBasicRewardNeverDeactivation](docs/ReferralProgramSenderBasicRewardNeverDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderCreditReward](docs/ReferralProgramSenderCreditReward.md)
 - [com.winwinkit.client.models.ReferralProgramSenderCreditRewardActivation](docs/ReferralProgramSenderCreditRewardActivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderCreditRewardDeactivation](docs/ReferralProgramSenderCreditRewardDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderCreditRewardIntervalDeactivation](docs/ReferralProgramSenderCreditRewardIntervalDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderCreditRewardNeverDeactivation](docs/ReferralProgramSenderCreditRewardNeverDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderGooglePlayPromoCodeReward](docs/ReferralProgramSenderGooglePlayPromoCodeReward.md)
 - [com.winwinkit.client.models.ReferralProgramSenderGooglePlayPromoCodeRewardActivation](docs/ReferralProgramSenderGooglePlayPromoCodeRewardActivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderGooglePlayPromoCodeRewardDeactivation](docs/ReferralProgramSenderGooglePlayPromoCodeRewardDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderGooglePlayPromoCodeRewardIntervalDeactivation](docs/ReferralProgramSenderGooglePlayPromoCodeRewardIntervalDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderGooglePlayPromoCodeRewardNeverDeactivation](docs/ReferralProgramSenderGooglePlayPromoCodeRewardNeverDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderOfferCodeReward](docs/ReferralProgramSenderOfferCodeReward.md)
 - [com.winwinkit.client.models.ReferralProgramSenderOfferCodeRewardActivation](docs/ReferralProgramSenderOfferCodeRewardActivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderOfferCodeRewardDeactivation](docs/ReferralProgramSenderOfferCodeRewardDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderOfferCodeRewardIntervalDeactivation](docs/ReferralProgramSenderOfferCodeRewardIntervalDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderOfferCodeRewardNeverDeactivation](docs/ReferralProgramSenderOfferCodeRewardNeverDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderRevenueCatEntitlementReward](docs/ReferralProgramSenderRevenueCatEntitlementReward.md)
 - [com.winwinkit.client.models.ReferralProgramSenderRevenueCatEntitlementRewardActivation](docs/ReferralProgramSenderRevenueCatEntitlementRewardActivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderRevenueCatEntitlementRewardDeactivation](docs/ReferralProgramSenderRevenueCatEntitlementRewardDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderRevenueCatEntitlementRewardIntervalDeactivation](docs/ReferralProgramSenderRevenueCatEntitlementRewardIntervalDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderRevenueCatEntitlementRewardNeverDeactivation](docs/ReferralProgramSenderRevenueCatEntitlementRewardNeverDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderRevenueCatOfferingReward](docs/ReferralProgramSenderRevenueCatOfferingReward.md)
 - [com.winwinkit.client.models.ReferralProgramSenderRevenueCatOfferingRewardActivation](docs/ReferralProgramSenderRevenueCatOfferingRewardActivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderRevenueCatOfferingRewardDeactivation](docs/ReferralProgramSenderRevenueCatOfferingRewardDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderRevenueCatOfferingRewardIntervalDeactivation](docs/ReferralProgramSenderRevenueCatOfferingRewardIntervalDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderRevenueCatOfferingRewardNeverDeactivation](docs/ReferralProgramSenderRevenueCatOfferingRewardNeverDeactivation.md)
 - [com.winwinkit.client.models.ReferralProgramSenderRewards](docs/ReferralProgramSenderRewards.md)
 - [com.winwinkit.client.models.RevenueCatEntitlementReward](docs/RevenueCatEntitlementReward.md)
 - [com.winwinkit.client.models.RevenueCatOfferingReward](docs/RevenueCatOfferingReward.md)
 - [com.winwinkit.client.models.User](docs/User.md)
 - [com.winwinkit.client.models.UserBasicRewardActive](docs/UserBasicRewardActive.md)
 - [com.winwinkit.client.models.UserBasicRewardExpired](docs/UserBasicRewardExpired.md)
 - [com.winwinkit.client.models.UserClaimCodeEligibility](docs/UserClaimCodeEligibility.md)
 - [com.winwinkit.client.models.UserClaimCodeRequest](docs/UserClaimCodeRequest.md)
 - [com.winwinkit.client.models.UserClaimCodeResponse](docs/UserClaimCodeResponse.md)
 - [com.winwinkit.client.models.UserClaimCodeResponseData](docs/UserClaimCodeResponseData.md)
 - [com.winwinkit.client.models.UserCreateRequest](docs/UserCreateRequest.md)
 - [com.winwinkit.client.models.UserCreditRewardActive](docs/UserCreditRewardActive.md)
 - [com.winwinkit.client.models.UserCreditRewardExpired](docs/UserCreditRewardExpired.md)
 - [com.winwinkit.client.models.UserGooglePlayPromoCodeRewardActive](docs/UserGooglePlayPromoCodeRewardActive.md)
 - [com.winwinkit.client.models.UserGooglePlayPromoCodeRewardExpired](docs/UserGooglePlayPromoCodeRewardExpired.md)
 - [com.winwinkit.client.models.UserGrantRewardRequest](docs/UserGrantRewardRequest.md)
 - [com.winwinkit.client.models.UserGrantRewardResponse](docs/UserGrantRewardResponse.md)
 - [com.winwinkit.client.models.UserGrantRewardResponseData](docs/UserGrantRewardResponseData.md)
 - [com.winwinkit.client.models.UserOfferCodeRewardActive](docs/UserOfferCodeRewardActive.md)
 - [com.winwinkit.client.models.UserOfferCodeRewardExpired](docs/UserOfferCodeRewardExpired.md)
 - [com.winwinkit.client.models.UserReferredBy](docs/UserReferredBy.md)
 - [com.winwinkit.client.models.UserResponse](docs/UserResponse.md)
 - [com.winwinkit.client.models.UserResponseData](docs/UserResponseData.md)
 - [com.winwinkit.client.models.UserRevenueCatEntitlementRewardActive](docs/UserRevenueCatEntitlementRewardActive.md)
 - [com.winwinkit.client.models.UserRevenueCatEntitlementRewardExpired](docs/UserRevenueCatEntitlementRewardExpired.md)
 - [com.winwinkit.client.models.UserRevenueCatOfferingRewardActive](docs/UserRevenueCatOfferingRewardActive.md)
 - [com.winwinkit.client.models.UserRevenueCatOfferingRewardExpired](docs/UserRevenueCatOfferingRewardExpired.md)
 - [com.winwinkit.client.models.UserRewards](docs/UserRewards.md)
 - [com.winwinkit.client.models.UserRewardsActive](docs/UserRewardsActive.md)
 - [com.winwinkit.client.models.UserRewardsExpired](docs/UserRewardsExpired.md)
 - [com.winwinkit.client.models.UserRewardsGranted](docs/UserRewardsGranted.md)
 - [com.winwinkit.client.models.UserStats](docs/UserStats.md)
 - [com.winwinkit.client.models.UserWithdrawCreditsRequest](docs/UserWithdrawCreditsRequest.md)
 - [com.winwinkit.client.models.UserWithdrawCreditsResponse](docs/UserWithdrawCreditsResponse.md)
 - [com.winwinkit.client.models.UserWithdrawCreditsResponseData](docs/UserWithdrawCreditsResponseData.md)
 - [com.winwinkit.client.models.UserWithdrawCreditsResult](docs/UserWithdrawCreditsResult.md)


<a id="documentation-for-authorization"></a>
## Documentation for Authorization


Authentication schemes defined for the API:
<a id="x-api-key"></a>
### x-api-key

- **Type**: API key
- **API key parameter name**: x-api-key
- **Location**: HTTP header
