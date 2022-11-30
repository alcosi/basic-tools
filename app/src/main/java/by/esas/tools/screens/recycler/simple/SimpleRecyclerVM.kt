package by.esas.tools.screens.recycler.simple

import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import by.esas.tools.App
import by.esas.tools.base.AppVM
import by.esas.tools.recycler.simpleItemAdapter.SimpleItemAdapter
import by.esas.tools.recycler.simpleItemAdapter.SimpleItemModel
import by.esas.tools.screens.recycler.FirstEntity
import java.util.*
import java.util.logging.Handler
import javax.inject.Inject
import kotlin.concurrent.schedule

class SimpleRecyclerVM @Inject constructor() : AppVM() {

    val newEntity: MutableLiveData<String> = MutableLiveData("")
    val withPositionFlag: MutableLiveData<Boolean> = MutableLiveData(false)
    val makeItemsSelectable: MutableLiveData<Boolean> = MutableLiveData(false)
    val isSingleItemSelectable: MutableLiveData<Boolean> = MutableLiveData(true)
    var currentListIsFirst: Boolean = true
    var currentAlignment: Int = View.TEXT_ALIGNMENT_CENTER
    val firstList: List<FirstEntity> = listOf(
        FirstEntity("Alpha"),
        FirstEntity("Beta"),
        FirstEntity("Gamma"),
        FirstEntity("Delta"),
        FirstEntity("Epsilon"),
        FirstEntity("Zeta"),
        FirstEntity("Eta"),
        FirstEntity("Theta"),
        FirstEntity("Iota"),
        FirstEntity("Cappa"),
        FirstEntity("Lambda"),
        FirstEntity("Mi"),
        FirstEntity("Ni"),
        FirstEntity("Csi"),
        FirstEntity("Omicron"),
        FirstEntity("Pi"),
        FirstEntity("Rho"),
        FirstEntity("Sigma"),
        FirstEntity("Tau"),
        FirstEntity("Ipsilon"),
        FirstEntity("Phi"),
        FirstEntity("Chi"),
        FirstEntity("Psi"),
        FirstEntity("Omega")
    )
    val secondList: List<FirstEntity> = listOf(
        FirstEntity("One"),
        FirstEntity("Two"),
        FirstEntity("Three"),
        FirstEntity("Four"),
        FirstEntity("Five"),
        FirstEntity("Six"),
        FirstEntity("Seven"),
        FirstEntity("Eight"),
        FirstEntity("Nine"),
        FirstEntity("Ten"),
        FirstEntity("Eleven"),
        FirstEntity("Twelve")
    )

    val adapter: SimpleItemAdapter = SimpleItemAdapter(
        onItemClick = { item ->
            if (withPositionFlag.value == false)
                Toast.makeText(App.instance, "simpleAdapter: Click on item ${item.name}", Toast.LENGTH_SHORT).show()
        },
        onItemClickPosition = { pos, item ->
            doOnClickWithPosition(pos, item)
        },
        onItemLongClick = { item ->
            if (withPositionFlag.value == false)
                Toast.makeText(App.instance, "simpleAdapter: Long click on item ${item.name}", Toast.LENGTH_SHORT)
                    .show()
        },
        onItemLongClickPosition = { pos, item ->
            if (withPositionFlag.value == true)
                Toast.makeText(
                    App.instance,
                    "simpleAdapter: Long click on item ${item.name} with position $pos",
                    Toast.LENGTH_SHORT
                ).show()
        }
    )

    private fun doOnClickWithPosition(pos: Int, item: SimpleItemModel) {
        if (withPositionFlag.value == true)
            Toast.makeText(
                App.instance,
                "simpleAdapter: Click on item ${item.name} with position $pos",
                Toast.LENGTH_SHORT
            ).show()
        if (makeItemsSelectable.value == true) {
            if (item.isChoosed) adapter.setItemUnpicked(pos)
            else adapter.setItemPicked(pos, isSingleItemSelectable.value == true)
        }
    }

    fun addItem() {
        val name = newEntity.value ?: "Empty"
        adapter.itemList.get(adapter.itemList.lastIndex).isLast = false
        adapter.addItem(
            SimpleItemModel(
                name = name,
                shortName = name,
                isChoosed = false,
                isLast = true,
                textAlignment = currentAlignment
            )
        )
    }

    fun clearItems() {
        adapter.cleanItems()
    }

    fun setAnotherList() {
        adapter.setItems(
            if (currentListIsFirst) {
                currentListIsFirst = false
                val lastIndex = secondList.lastIndex
                secondList.mapIndexed { index, entity ->
                    entity.mapToSimple(lastIndex == index, currentAlignment)
                }
            } else {
                currentListIsFirst = true
                val lastIndex = firstList.lastIndex
                firstList.mapIndexed { index, entity ->
                    entity.mapToSimple(lastIndex == index, currentAlignment)
                }
            }
        )
    }
}

fun FirstEntity.mapToSimple(isLast: Boolean, alignment: Int): SimpleItemModel {
    return SimpleItemModel(name = name, shortName = name, isChoosed = false, isLast = isLast, textAlignment = alignment)
}