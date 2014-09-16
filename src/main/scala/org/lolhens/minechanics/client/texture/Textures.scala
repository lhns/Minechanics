package org.lolhens.minechanics.client.texture

import collection._
import com.dafttech.nio.file.PathUtil
import org.lolhens.minechanics.core.FMLMinechanicsPlugin
import org.lolhens.minechanics.core.util.LogHelper
import com.dafttech.classfile.URLClassLocation
import org.lolhens.minechanics.Minechanics
import net.minecraft.util.IIcon
import net.minecraft.client.renderer.texture.IIconRegister
import java.nio.file.Files
import java.nio.file.SimpleFileVisitor
import java.nio.file.Path
import java.nio.file.FileVisitResult
import java.nio.file.attribute.BasicFileAttributes
import cpw.mods.fml.relauncher.SideOnly
import cpw.mods.fml.relauncher.Side

object Textures {
  val textureLocation = Minechanics.location.resolve(s"assets/${Minechanics.Id}/textures")
  val textureFiles = mutable.MutableList[Path]()

  val icons = mutable.Map[String, IIcon]()

  def load(path: String) = {
    Files.walkFileTree(textureLocation, new SimpleFileVisitor[Path]() {
      override def visitFile(file: Path, attrs: BasicFileAttributes) = {
        if (file.getFileName.toString.toLowerCase.endsWith(".png")) textureFiles += file
        FileVisitResult.CONTINUE
      }
    })
  }

  def register(path: String, iconRegister: IIconRegister) {
    for (textureFile <- textureFiles) {
      println(textureLocation.relativize(textureFile).getParent)
      val textureFileName = textureFile.getFileName.toString
      val textureName = textureFileName.substring(0, textureFileName.lastIndexOf("."))
      //icons +=
    }
  }
}