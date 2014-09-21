package org.lolhens.minechanics.common.block.material

import net.minecraft.block.material.{MapColor, Material}
import org.lolhens.minechanics.common.block.material.MaterialCustom._

/**
 * Created by LolHens on 21.09.2014.
 */
class MaterialCustom() extends Material(MapColor.airColor) {
  var block: ICustomMaterial = null

  override def getCanBlockGrass: Boolean = if (block == null) super.getCanBlockGrass else !block.getCanBlockGrass

  override def getMaterialMobility: Int = if (block == null) super.getMaterialMobility else block.getMobilityFlag

  override def isOpaque: Boolean = if (block == null) super.isOpaque else block.isOpaqueCube

  override def isLiquid: Boolean = if (block == null) super.isLiquid else block.isLiquid

  override def isSolid: Boolean = if (block == null) super.isSolid else block.isSolid

  override def blocksMovement: Boolean = if (block == null) super.blocksMovement else block.getBlocksMovement

  override def getCanBurn: Boolean = if (block == null) super.getCanBurn else block.isFlammable

  override def isReplaceable: Boolean = if (block == null) super.isReplaceable else block.isReplaceable

  override def isToolNotRequired: Boolean = if (block == null) super.isToolNotRequired else !block.isToolRequired

  override def isAdventureModeExempt: Boolean = if (block == null) super.isAdventureModeExempt else block.isAdventureModeExempt

  override def getMaterialMapColor: MapColor = if (block == null) super.getMaterialMapColor else block.getMapColor
}

object MaterialCustom {

  trait ICustomMaterial {
    def getCanBlockGrass: Boolean = false

    def getMobilityFlag: Int = 0

    def isOpaqueCube: Boolean = true

    def isLiquid: Boolean = false

    def isSolid: Boolean = true

    def getBlocksMovement: Boolean = true

    def isFlammable: Boolean = false

    def isReplaceable: Boolean = false

    def isToolRequired = false

    def isAdventureModeExempt: Boolean = false

    def getMapColor: MapColor = MapColor.airColor
  }

}