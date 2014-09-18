package org.lolhens.minechanics.core.storageaccess.nbt

import net.minecraft.nbt.NBTTagList
import org.lolhens.minechanics.core.storageaccess.StorageAccess

import scala.collection.JavaConversions._

class NBTList(override val obj: NBTTagList) extends ValidNBTObject[NBTTagList](obj) {
  override def apply(i: Int) = if (i >= obj.tagList.size || i < 0) NBTObject else fromAny(obj.tagList.get(i))

  override def foreach(f: StorageAccess => Unit) = {
    val iterator = obj.tagList.iterator();
    while (iterator.hasNext()) f(NBTObject.fromAny(iterator.next()))
  }

  override def map[B](f: (Any) => B): Set[B] = obj.tagList.toSet[Any].map(f)
}