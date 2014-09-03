package org.lolhens.minechanics.core.asm

import java.io.File
import com.google.gson.Gson
import java.io.Reader
import collection._
import collection.JavaConversions._
import org.lolhens.minechanics.core.json._
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode

class Transformations(reader: Reader) {
  val insnLists = mutable.Map[String, InsnList]()
  val methods = mutable.Map[String, MethodNode]()
  val triggers = mutable.Map[String, Trigger]()
  val transformations = mutable.Map[String, Transformation]()

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