package org.lolhens.minechanics.core.asm

import java.io.File
import com.google.gson.Gson
import java.io.Reader
import scala.collection.JavaConversions._
import org.lolhens.minechanics.core.json._

class TransformerReader(reader: Reader) {
  val insnLists = collection.mutable.Map[String, List[String]]()
  val methods = collection.mutable.Map[String, String]()
  val triggers = collection.mutable.Map[String, Trigger]()

  read

  private def read() = {
    type javaMap = java.util.Map[_, _]

    val root = new JsonMap(new Gson().fromJson(reader, classOf[javaMap]))

    for (insnList <- root.vars.insnLists) if (insnList.isInstanceOf[JsonMap]) parseInsnList(insnList.asInstanceOf[JsonMap])

    def parseInsnList(map: JsonObject) = {
      val name: String = map.name
      if (name != null) {
        val insns = for (insn <- map.insns) yield insn
      }
    }
  }
}