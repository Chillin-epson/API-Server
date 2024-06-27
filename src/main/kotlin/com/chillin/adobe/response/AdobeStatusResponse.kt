package com.chillin.adobe.response

data class AdobeStatusResponse(val _links: Links) {
    data class Links(val self: Self) {
        data class Self(val href: String)
    }
}