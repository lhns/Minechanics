package org.lolhens.minechanics.client.util

import com.dafttech.math.TrigonometryCache
import org.lolhens.minechanics.client.util.Vec3f._

/**
 * Created by LolHens on 21.09.2014.
 */
case class Vec3f(val tuple: (Float, Float, Float)) extends AnyVal {
  def x = tuple._1

  def y = tuple._2

  def z = tuple._3

  def +(vec: Vec3f) = new Vec3f(x + vec.x, y + vec.y, z + vec.z)

  def -(vec: Vec3f) = new Vec3f(x - vec.x, y - vec.y, z - vec.z)

  def *(vec: Vec3f) = new Vec3f(x * vec.x, y * vec.y, z * vec.z)

  def /(vec: Vec3f) = new Vec3f(x / vec.x, y / vec.y, z / vec.z)

  def rotate(rot: Vec3f, origin: Vec3f): Vec3f = {
    val x: Float = this.x - origin.x
    val y: Float = this.y - origin.x
    val z: Float = this.z - origin.z

    val sX: Float = trigonometryCache.sin(rot.x)
    val sY: Float = trigonometryCache.sin(rot.y)
    val sZ: Float = trigonometryCache.sin(rot.z)

    val cX: Float = trigonometryCache.cos(rot.x)
    val cY: Float = trigonometryCache.cos(rot.y)
    val cZ: Float = trigonometryCache.cos(rot.z)

    ((cZ * (cY * x + sY * (sX * y + cX * z)) - sZ * (cX * y - sX * z)) + origin.x,
      (sZ * (cY * x + sY * (sX * y + cX * z)) + cZ * (cX * y - sX * z)) + origin.y,
      (-sY * x + cY * (sX * y + cX * z)) + origin.z).asInstanceOf[Vec3f]
  }
}

object Vec3f {
  val trigonometryCache = new TrigonometryCache(8)

  implicit def toVec3f(tuple: (Float, Float, Float)) = new Vec3f(tuple)

  implicit def fromVec3f(vec: Vec3f) = vec.tuple
}