package org.lolhens.minechanics.client.render.r3d

import org.lolhens.minechanics.client.util.Vec3f

import scala.collection.mutable

/**
 * Created by LolHens on 21.09.2014.
 */
trait Model {
  val subModels = mutable.MutableList[Model]()
  var translation = new Vec3f((0, 0, 0))
  var rotation = new Vec3f((0, 0, 0))

  final def renderModel(translation: Vec3f)(rotation: Vec3f = new Vec3f((0, 0, 0))): Unit = {
    renderModel(this.translation + translation)(this.rotation + rotation)
    //subModels.foreach(_.renderModel(vertexList))
  }

  def render
}

object Model {
  def getIconUV(icon: IIcon, iconMinU: Float, iconMinV: Float, iconMaxU: Float, iconMaxV: Float) = {
    val minU: Float = if (iconMinU < 0) icon.getMinU else if (iconMinU > 1) icon.getMaxU else icon.getInterpolatedU(iconMinU * 16)
    val minV: Float = if (iconMinV < 0) icon.getMinV else if (iconMinV > 1) icon.getMaxV else icon.getInterpolatedV(iconMinV * 16)
    val maxU: Float = if (iconMaxU < 0) icon.getMinU else if (iconMaxU > 1) icon.getMaxU else icon.getInterpolatedU(iconMaxU * 16)
    val maxV: Float = if (iconMaxV < 0) icon.getMinV else if (iconMaxV > 1) icon.getMaxV else icon.getInterpolatedV(iconMaxV * 16)
    (minU, minV, maxU, maxV)
  }
}