package org.lolhens.minechanics.core.asm

import java.io.File
import com.google.gson.Gson
import java.io.Reader

class TransformerReader(reader: Reader) {
  val insnLists = collection.mutable.Map[String, List[String]]()
  val methods = collection.mutable.Map[String, String]()
  val triggers = collection.mutable.Map[String, Trigger]()

  read

  private def read() = {
    type javaMap = java.util.Map[_, _]
    type javaList = java.util.List[_]

    val root = new Gson().fromJson(reader, classOf[javaMap])

    val vars = root.get("vars").asInstanceOf[javaMap]
    if (vars != null) {
      val insnLists = vars.get("insnLists").asInstanceOf[javaList]
      if (insnLists != null) {

      }
    }
  }
}