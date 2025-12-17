
# AppStoreOfferCode

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.String** | The offer code id. |  |
| **name** | **kotlin.String** | The offer code name. |  |
| **customerEligibilities** | **kotlin.collections.List&lt;kotlin.String&gt;** | The offer code customer eligibilities. |  |
| **offerEligibility** | [**inline**](#OfferEligibility) | The offer code offer eligibility. |  |
| **duration** | [**inline**](#Duration) | The offer code duration. |  |
| **offerMode** | [**inline**](#OfferMode) | The offer code offer mode. |  |
| **numberOfPeriods** | **kotlin.Int** | The offer code number of periods. |  |
| **prices** | [**kotlin.collections.List&lt;AppStorePrice&gt;**](AppStorePrice.md) | The offer code prices. |  |


<a id="OfferEligibility"></a>
## Enum: offer_eligibility
| Name | Value |
| ---- | ----- |
| offerEligibility | STACK_WITH_INTRO_OFFERS, REPLACE_INTRO_OFFERS |


<a id="Duration"></a>
## Enum: duration
| Name | Value |
| ---- | ----- |
| duration | THREE_DAYS, ONE_WEEK, TWO_WEEKS, ONE_MONTH, TWO_MONTHS, THREE_MONTHS, SIX_MONTHS, ONE_YEAR |


<a id="OfferMode"></a>
## Enum: offer_mode
| Name | Value |
| ---- | ----- |
| offerMode | PAY_AS_YOU_GO, PAY_UP_FRONT, FREE_TRIAL |



