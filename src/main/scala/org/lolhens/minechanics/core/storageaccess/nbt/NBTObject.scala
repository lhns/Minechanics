package org.lolhens.minechanics.core.storageaccess.nbt

import org.lolhens.minechanics.core.storageaccess.EmptyStorageAccess
import org.lolhens.minechanics.core.storageaccess.StorageAccess

object NBTObject extends EmptyStorageAccess {
  def fromAny(any: Any): StorageAccess = any match {
    case null => NBTObject
    //case any: java.util.List[_] => new JsonList(any)
    //case any: java.util.Map[_, _] => new JsonMap(any)
    //case any: String => new JsonString(any)
    //case any: Double => new JsonDouble(any)
    //case any: Boolean => new JsonBoolean(any);
    case _ => NBTObject
  }
}