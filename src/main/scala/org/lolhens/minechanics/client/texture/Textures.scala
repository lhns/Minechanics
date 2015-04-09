package org.lolhens.minechanics.client.texture

import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{FileVisitResult, Files, Path, SimpleFileVisitor}

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.client.event.TextureStitchEvent
import org.lolhens.minechanics.Minechanics

import scala.collection._

object Textures {
  //val textureLocation = Minechanics.location.resolve(s"assets/${Minechanics.Id}/textures")
  val textureFiles = mutable.MutableList[Path]()

  //val icons = mutable.Map[String, IIcon]()

  /*def load(path: String) = {
    Files.walkFileTree(textureLocation, new SimpleFileVisitor[Path]() {
      override def visitFile(file: Path, attrs: BasicFileAttributes) = {
        if (file.getFileName.toString.toLowerCase.endsWith(".png")) textureFiles += file
        FileVisitResult.CONTINUE
      }
    })
  }

  def register(path: String, iconRegister: IIconRegister) = {
    for (textureFile <- textureFiles) {
      val texturePath = textureLocation.relativize(textureFile).getParent.toString.toLowerCase
      if (texturePath == path) {
        val textureFileName = textureFile.getFileName.toString
        val textureName = textureFileName.substring(0, textureFileName.lastIndexOf("."))
        if (!textureName.startsWith("anim")) icons += s"$path.${textureName.toLowerCase}" -> iconRegister.registerIcon(s"${Minechanics.Id}:$textureName")
      }
    }
  }*/

  def notifyRegister(iterator: java.util.Iterator[_]): Unit = {
    while (iterator.hasNext) {
      val any: Any = iterator.next
      any match {
        case any: IOnTextureRegistered => any.onTextureRegistered
        case _ =>
      }
    }
  }

  @SubscribeEvent
  def onTextureStitchPre(event: TextureStitchEvent.Pre) = {
    val textureMap = event.map
    /*val textureType = textureMap.getTextureType
    register(textureType match {
      case 0 => "blocks"
      case 1 => "items"
    }, textureMap)
    notifyRegister(textureType match {
      case 0 => Block.blockRegistry.iterator
      case 1 => Item.itemRegistry.iterator
    })*/
  }

  trait IOnTextureRegistered {
    def onTextureRegistered: Unit
  }

}