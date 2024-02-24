package com.example.elogapp.repository.model

import com.example.elogapp.repository.responses.load.Consignees
import com.example.elogapp.repository.responses.load.Shippers

data class ShipperConsigneeList(
    var type: String,
    var pos: Int,
    var shipper: Shippers?,
    var consignee: Consignees?
) {

    constructor() : this("", -1, null , null)

}