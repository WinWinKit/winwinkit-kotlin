
# UserAffiliate

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.String** | The unique identifier of the affiliate. |  |
| **status** | [**inline**](#Status) | Where the affiliate stands with this app. &#x60;banned&#x60; is terminal; &#x60;rejected&#x60;, &#x60;withdrawn&#x60;, &#x60;archived&#x60; and &#x60;deactivated&#x60; describe affiliates who may apply again. |  |
| **codes** | [**kotlin.collections.List&lt;UserAffiliateCode&gt;**](UserAffiliateCode.md) | The codes the affiliate can currently promote, oldest first. Empty until they create one — an approved affiliate with no code yet is a normal state. |  |
| **stats** | [**UserAffiliateStats**](UserAffiliateStats.md) | The affiliate&#39;s totals on this app. |  |


<a id="Status"></a>
## Enum: status
| Name | Value |
| ---- | ----- |
| status | pending, withdrawn, approved, rejected, archived, deactivated, banned |



