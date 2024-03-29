
import android.os.Environment
import android.util.Log
import com.mirsery.open.widget.complex.ComplexType
import com.mirsery.open.widget.program.SimpleProgram
import com.mirsery.open.widget.program.StandProgram
import java.io.File

object  ComplexPlayerList {

    private val resourcePath =
        Environment.getExternalStorageDirectory().absolutePath + "/ScreenFree"


    private var playList: MutableList<StandProgram> = ArrayList()

    private var cur = 0

    init {
        refreshPlayList()
        Log.i("simple_player", "resource is $resourcePath")

    }

    private fun refreshPlayList() {
        playList.clear()
        val f = File(resourcePath)

        if (!f.exists()) {
            f.mkdir()
        }


        f.listFiles()?.forEach {
            if (it.path.endsWith(".png") || it.path.endsWith(".jpg")) {

                val map1 :MutableMap<String, SimpleProgram> = HashMap()
                map1["A"] = SimpleProgram(path = it.path, type = 0)
                map1["B"] = SimpleProgram(path = it.path, type = 0)

                playList.add(StandProgram(ComplexType.ORow2ColTemplate.toString(),5L,map1))

                val map2 :MutableMap<String,SimpleProgram> = HashMap()
                map2["A"] = SimpleProgram(path = it.path, type = 0)
                map2["B"] = SimpleProgram(path = it.path, type = 0)

                playList.add(StandProgram(ComplexType.TRowOColTemplate.toString(),5L,map2))

            } else if (it.path.endsWith(".mp4")) {
                val map :MutableMap<String,SimpleProgram> = HashMap()
                map["A"] = SimpleProgram(path = it.path, type = 1)
                playList.add(StandProgram(ComplexType.SingleVerticalTemplate.toString(),10L,map))
            }
        }
    }

    fun nextProgram(): StandProgram? {

        if (playList.size < 1) {
            refreshPlayList()
            return null
        }

        if (cur >= playList.size) {
            refreshPlayList()
            cur = 0
        }
        cur++
        return playList[cur - 1]
    }
}

