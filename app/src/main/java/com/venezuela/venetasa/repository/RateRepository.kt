package com.venezuela.venetasa.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.venezuela.venetasa.db.RateDao
import com.venezuela.venetasa.model.Rate
import com.venezuela.venetasa.remote.RateRemoteDataSource
import com.venezuela.venetasa.utils.Resource
import kotlinx.coroutines.Dispatchers
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import javax.inject.Inject

class RateRepository @Inject constructor(
    private val rateDao: RateDao,
    private val rateRemoteDataSource: RateRemoteDataSource
) {

    private val _networkCallStatus = MutableLiveData<Resource.Status>()
    val networkCallStatus: LiveData<Resource.Status> get() = _networkCallStatus

    fun queryAllRates(): LiveData<List<Rate>> =
        liveData(Dispatchers.IO) {
            emitSource(rateDao.queryRates())
        }

    suspend fun fetchRates() {

        _networkCallStatus.postValue(Resource.Status.LOADING)

        val response = rateRemoteDataSource.requestRates()

        _networkCallStatus.postValue(response.status)

        if(response.status == Resource.Status.SUCCESS){
            val document = response.data
            document?.let {
                val elements = it.getElementsByTag("table").first().getElementsByTag("tr")
                val rates = mutableListOf<Rate>()
                for(i in 1.until(elements.size)){
                    val childNodes = elements[i].childNodes()
                    val rateTitle = textNode(childNodes[0].childNodes())
                    val rateValue = textNode(childNodes[1].childNodes())
                        .text()
                        .replace(",", ".")
                        .replace("Bs. ", "")
                        .toDouble()
                    val triangle = findTriangle(childNodes[1] as Element)
                    val nextUpdateIcon = nextUpdateIcon(childNodes[1] as Element)
                    rates.add(Rate(0, rateTitle.text(), rateValue, triangle, nextUpdateIcon))
                }
                rateDao.deleteAllRates()
                rateDao.insertAllRates(rates)
            }
        }

    }

    private fun nextUpdateIcon(element: Element): String{
        val elementsByAttribute = element.getElementsByAttribute("src")
        return if(elementsByAttribute.size != 0)
            elementsByAttribute[0].attr("src")
        else
            ""
    }

    private fun findTriangle(element: Element): String {
        return if(element.getElementsMatchingText("▼").size != 0)
            "▼"
        else
            "▲"
    }

    private fun textNode(nodeList: List<Node>): TextNode {
        val node = nodeList[0]
        if(node is TextNode)
            return node
        return textNode(node.childNodes())
    }
}