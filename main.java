var inv;
var sel = -1;
var status = -1;

var maple = 4001126;

function action(mode, type, selection) {
   if (mode == 1) {
      status++;
   } else {
      cm.dispose();
      return;
   }
   switch (status) {
      case 0:
         var message = "일반강화를 하러 오셨나보군요.#b";
         message += "\r\n#L1#강화를 한다.";
         message += "\r\n\#L2#설명을 듣는다.";
         message += "\r\n\r\n#L11#단풍잎 강화를 한다.";
         message += "\r\n#L12#설명을 듣는다.";
         cm.sendSimple(message);
         break;
      case 1:
         if (selection == 1) {
            getList();
         } else if (selection == 2) {
            var message = "아이템의 착용 레벨에 따라 강화에 필요한 메소와 강화할 수 있는 수치가 달라집니다.";
            message += "\r\n\r\n공격력과 마력은 존재하면 +1, 50당 추가로 +1 상승합니다.";
            message += "\r\n\r\n1성 ~ 5성 : 올스텟 +2\r\n6성 ~ 10성 : 올스텟 +3\r\n11성 ~ 15성 : 올스텟 +5";
            message += "\r\n\r\n착용 레벨 1 ~ 29 : 최대 3성\r\n착용 레벨 30 ~ 49 : 최대 5성\r\n착용 레벨 50 ~ 69 : 최대 7성\r\n착용 레벨 70 ~ 99 : 최대 9성\r\n착용 레벨 100 ~ 119 : 최대 12성\r\n착용 레벨 120 ~ 140 : 최대 15성";
            cm.sendOk(message);
            status = 100;
         } else if (selection == 11) {
            getMapleList();
            status = 9;
         } else if (selection == 12) {
            var message = "#i" + maple + ":# #b#z" + maple + "##k이 필요하며 성공률은 100% 입니다.";
            message += "\r\n성공시 올스텟이 +3 증가합니다.";
            message += "\r\n\r\n50레벨 미만 : 최대 5번, 재료 50개";
            message += "\r\n100레벨 미만 : 최대 10번, 재료 200개";
            message += "\r\n150레벨 미만 : 최대 15번, 재료 500개";
            message += "\r\n200레벨 미만 : 최대 20번, 재료 1000개";
            cm.sendOk(message);
            status = 100;
         }
         break;
      case 2:
         if (selection > 0) sel = selection;
         if (Inv.getItem(sel) == null) {
            cm.sendOk("무언가 버그가 발생했습니다.");
            cm.dispose();
            return;
         }
         var itemcode = Inv.getItem(sel).getItemId();
         if (cm.isCash(itemcode)) {
            cm.sendOk("캐시 아이템은 강화할 수 없습니다.");
            cm.dispose();
            return;
         }
         if (!checkid(itemcode)) {
            cm.sendOk("선택한 아이템은 강화할 수 없습니다.");
            cm.dispose();
            return;
         }
         var item = Inv.getItem(sel);
         var watk = item.getWatk();
         var matk = item.getMatk();
         var str = item.getStr();
         var dex = item.getDex();
         var int_ = item.getInt();
         var luk = item.getLuk();
         var extra = item.getExtraForce();
         var maxlv = getLv(itemcode);
         var message = "- 아이템 : #i" + itemcode + "# #t" + itemcode + "# < " + extra + "강 >\r\n\r\n";
         if (watk > 0) {
            message += "- 공격력 : " + watk + " -> #b" + (watk + Math.floor(watk / 50) + 1) + " #r(+" + (Math.floor(watk / 50) + 1) + ")#k\r\n";
         } else {
            message += "- 공격력 : " + watk + " -> #b" + watk + "#k\r\n";
         }
         if (matk > 0) {
            message += "- 마력 : " + matk + " -> #b" + (matk + Math.floor(matk / 50) + 1) + " #r(+" + (Math.floor(matk / 50) + 1) + ")#k\r\n";
         } else {
            message += "- 마력 : " + matk + " -> #b" + matk + "#k\r\n";
         }
         var upg = getAll(extra);
         message += "- 힘 : " + str + " -> #b" + (str + upg) + " #r(+" + upg + ")#k\r\n";
         message += "- 덱 : " + dex + " -> #b" + (dex + upg) + " #r(+" + upg + ")#k\r\n";
         message += "- 인 : " + int_ + " -> #b" + (int_ + upg) + " #r(+" + upg + ")#k\r\n";
         message += "- 럭 : " + luk + " -> #b" + (luk + upg) + " #r(+" + upg + ")#k\r\n";
         message += "\r\n- 필요한 메소 : " + cm.getPlayer().getBanJum(getMeso(itemcode, extra));
         message += "\r\n- 강화 성공 확률 : " + getChance(extra) + "%\r\n";
         if (extra < maxlv) {
            message += "\r\n#r- 선택한 아이템을 강화 하시겠습니까? (최대 " + maxlv + "강)";
            cm.sendYesNo(message);
         } else {
            message += "\r\n#r- ※ 선택한 아이템으로 더는 강화를 진행할 수 없어요. ※";
            cm.sendOk(message);
            status = 100;
         }
         break;
      case 3:
         if (Inv.getItem(sel) == null) {
            cm.sendOk("무언가 버그가 발생했습니다.");
            cm.dispose();
            return;
         }
         var itemcode = Inv.getItem(sel).getItemId();
         if (cm.isCash(itemcode)) {
            cm.sendOk("캐시 아이템은 강화할 수 없습니다.");
            cm.dispose();
            return;
         }
         if (!checkid(itemcode)) {
            cm.sendOk("선택한 아이템은 강화할 수 없습니다.");
            cm.dispose();
            return;
         }
         var item = Inv.getItem(sel);
         var extra = item.getExtraForce();
         var maxlv = getLv(itemcode);
         if (extra < maxlv) {
            var conMeso = getMeso(itemcode, extra);
            if (cm.getMeso() < conMeso) {
               cm.sendOk("강화를 하기 위해선 #r" + cm.getPlayer().getBanJum(conMeso) + " 메소#k가 필요합니다.");
               cm.dispose();
               return;
            }
            cm.gainMeso(-conMeso);
            var successRand = Math.floor(Math.random() * 100);
            var Text = "성공";
            var Rate = getAll(extra);
            if (successRand < getChance(extra)) {
               cm.getPlayer().itemSuccessEffect();
               if (item.getWatk() > 0) {
                  item.setWatk(item.getWatk() + Math.floor(item.getWatk() / 50) + 1);
               }
               if (item.getMatk() > 0) {
                  item.setMatk(item.getMatk() + Math.floor(item.getMatk() / 50) + 1);
               }
               item.setStr(item.getStr() + Rate);
               item.setDex(item.getDex() + Rate);
               item.setInt(item.getInt() + Rate);
               item.setLuk(item.getLuk() + Rate);
               item.setExtraForce(extra + 1);
               item.setOwner((extra + 1) + "강");
               cm.getPlayer().forceReAddItem(item, cm.getInvType(1));
            } else {
               cm.getPlayer().itemFailEffect();
               Text = "실패";
            }
            var watk = item.getWatk();
            var matk = item.getMatk();
            var str = item.getStr();
            var dex = item.getDex();
            var int_ = item.getInt();
            var luk = item.getLuk();
            extra = item.getExtraForce();
            var message = "- 아이템 : #i" + itemcode + "# #t" + itemcode + "# < " + extra + "강 >\r\n\r\n";
            if (watk > 0) {
               message += "- 공격력 : " + watk + " -> #b" + (watk + Math.floor(watk / 50) + 1) + " #r(+" + (Math.floor(watk / 50) + 1) + ")#k\r\n";
            } else {
               message += "- 공격력 : " + watk + " -> #b" + watk + "#k\r\n";
            }
            if (matk > 0) {
               message += "- 마력 : " + matk + " -> #b" + (matk + Math.floor(matk / 50) + 1) + " #r(+" + (Math.floor(matk / 50) + 1) + ")#k\r\n";
            } else {
               message += "- 마력 : " + matk + " -> #b" + matk + "#k\r\n";
            }
            var upg = getAll(extra);
            message += "- 힘 : " + str + " -> #b" + (str + upg) + " #r(+" + upg + ")#k\r\n";
            message += "- 덱 : " + dex + " -> #b" + (dex + upg) + " #r(+" + upg + ")#k\r\n";
            message += "- 인 : " + int_ + " -> #b" + (int_ + upg) + " #r(+" + upg + ")#k\r\n";
            message += "- 럭 : " + luk + " -> #b" + (luk + upg) + " #r(+" + upg + ")#k\r\n";
            message += "\r\n- 필요한 메소 : " + cm.getPlayer().getBanJum(getMeso(itemcode, extra));
            message += "\r\n- 강화 성공 확률 : " + getChance(extra) + "%\r\n";
            if (extra < maxlv) {
               message += "\r\n#r- 강화를 " + Text + "했습니다. 더 진행하시겠습니까? (최대 " + maxlv + "강)";
               cm.sendYesNo(message);
               status --;
            } else {
               message += "\r\n#r- ※ 선택한 아이템으로 더는 강화를 진행할 수 없어요. ※";
               cm.sendOk(message);
               status = 100;
            }
         } else {
            cm.sendOk("선택한 아이템으로 더는 강화를 진행할 수 없어요.");
            cm.dispose();
            return;
         }
         break;
      case 10:
         if (selection > 0) sel = selection;
         if (Inv.getItem(sel) == null) {
            cm.sendOk("무언가 버그가 발생했습니다.");
            cm.dispose();
            return;
         }
         var itemcode = Inv.getItem(sel).getItemId();
         if (cm.isCash(itemcode)) {
            cm.sendOk("캐시 아이템은 강화할 수 없습니다.");
            cm.dispose();
            return;
         }
         if (!checkid(itemcode)) {
            cm.sendOk("선택한 아이템은 강화할 수 없습니다.");
            cm.dispose();
            return;
         }
         var item = Inv.getItem(sel);
         var str = item.getStr();
         var dex = item.getDex();
         var int_ = item.getInt();
         var luk = item.getLuk();
         var extra = item.getExtraValue();
         var maxlv = getMapleLv(itemcode);
         var upg = 3;
         var message = "- 아이템 : #i" + itemcode + "# #t" + itemcode + "# < " + extra + "강 >\r\n\r\n";
         message += "- 힘 : " + str + " -> #b" + (str + upg) + " #r(+" + upg + ")#k\r\n";
         message += "- 덱 : " + dex + " -> #b" + (dex + upg) + " #r(+" + upg + ")#k\r\n";
         message += "- 인 : " + int_ + " -> #b" + (int_ + upg) + " #r(+" + upg + ")#k\r\n";
         message += "- 럭 : " + luk + " -> #b" + (luk + upg) + " #r(+" + upg + ")#k\r\n";
         message += "\r\n- 필요한 단풍잎 : " + getMapleMater(itemcode) + "개";
         message += "\r\n- 강화 성공 확률 : 100%\r\n";
         if (extra < maxlv) {
            message += "\r\n#r- 선택한 아이템을 강화 하시겠습니까? (최대 " + maxlv + "강)";
            cm.sendYesNo(message);
         } else {
            message += "\r\n#r- ※ 선택한 아이템으로 더는 강화를 진행할 수 없어요. ※";
            cm.sendOk(message);
            status = 100;
         }
         break;
      case 11:
         if (Inv.getItem(sel) == null) {
            cm.sendOk("무언가 버그가 발생했습니다.");
            cm.dispose();
            return;
         }
         var itemcode = Inv.getItem(sel).getItemId();
         if (cm.isCash(itemcode)) {
            cm.sendOk("캐시 아이템은 강화할 수 없습니다.");
            cm.dispose();
            return;
         }
         if (!checkid(itemcode)) {
            cm.sendOk("선택한 아이템은 강화할 수 없습니다.");
            cm.dispose();
            return;
         }
         var item = Inv.getItem(sel);
         var extra = item.getExtraValue();
         var maxlv = getMapleLv(itemcode);
         if (extra < maxlv) {
            var conMeso = getMeso(itemcode, extra);
            if (!cm.haveItem(maple, getMapleMater(itemcode))) {
               cm.sendOk("강화를 하기 위해선 단풍잎 #r" + getMapleMater(itemcode) + "개#k가 필요합니다.");
               cm.dispose();
               return;
            }
            cm.gainItem(maple, -getMapleMater(itemcode));
            var Rate = 3;
            cm.getPlayer().itemSuccessEffect();
            item.setStr(item.getStr() + Rate);
            item.setDex(item.getDex() + Rate);
            item.setInt(item.getInt() + Rate);
            item.setLuk(item.getLuk() + Rate);
            item.setExtraValue(extra + 1);
            item.setOwner("[" + (extra + 1) + "강]");
            cm.getPlayer().forceReAddItem(item, cm.getInvType(1));
            var str = item.getStr();
            var dex = item.getDex();
            var int_ = item.getInt();
            var luk = item.getLuk();
            extra = item.getExtraValue();
            var upg = 3;
            var message = "- 아이템 : #i" + itemcode + "# #t" + itemcode + "# < " + extra + "강 >\r\n\r\n";
            message += "- 힘 : " + str + " -> #b" + (str + upg) + " #r(+" + upg + ")#k\r\n";
            message += "- 덱 : " + dex + " -> #b" + (dex + upg) + " #r(+" + upg + ")#k\r\n";
            message += "- 인 : " + int_ + " -> #b" + (int_ + upg) + " #r(+" + upg + ")#k\r\n";
            message += "- 럭 : " + luk + " -> #b" + (luk + upg) + " #r(+" + upg + ")#k\r\n";
            message += "\r\n- 필요한 단풍잎 : " + getMapleMater(itemcode) + "개";
            message += "\r\n- 강화 성공 확률 : 100%\r\n";
            if (extra < maxlv) {
               message += "\r\n#r- 강화를 성공했습니다. 더 진행하시겠습니까? (최대 " + maxlv + "강)";
               cm.sendYesNo(message);
               status --;
            } else {
               message += "\r\n#r- ※ 선택한 아이템으로 더는 강화를 진행할 수 없어요. ※";
               cm.sendOk(message);
               status = 100;
            }
         } else {
            cm.sendOk("선택한 아이템으로 더는 강화를 진행할 수 없어요.");
            cm.dispose();
            return;
         }
         break;
      default:
         cm.dispose();
         cm.openNpc(cm.getNpc());
         break;
   }
}

function getList() {
   Inv = cm.getInventory(1);
   var checkInv = false;
   var message = "모험가님이 소지중인 일반아이템 목록입니다.\r\n";
   for (i = 0; i <= Inv.getSlotLimit(); i++) {
      if (Inv.getItem(i) == null) {
         continue;
      }
      if (cm.isCash(Inv.getItem(i).getItemId()) || !checkid(Inv.getItem(i).getItemId())) {
         continue;
      }
      checkInv = true;
      var geti = Inv.getItem(i);
      message += "#L" + i + "##i" + geti.getItemId() + ":# #b#z" + geti.getItemId() + "##k - " + geti.getExtraForce() + "강\r\n";
   }
   if (!checkInv) {
      cm.sendOk("아이템을 가지고 있는지 다시 한 번 확인해주세요.");
      cm.dispose();
      return;
   }
   cm.sendSimple(message);
}

function getReq(itemid) {
   return cm.getReqLevel(itemid);
}

function getLv(itemid) {
   var lv = getReq(itemid);
   if (lv < 30) {
      return 3;
   } else if (lv < 50) {
      return 5;
   } else if (lv < 70) {
      return 7;
   } else if (lv < 100) {
      return 9;
   } else if (lv < 120) {
      return 12;
   } else if (lv < 140) {
      return 15;
   }
   return 3;
}

function getChance(value) {
   switch (value) {
      case 0:
      case 1:
         return 50;
      case 2:
      case 3:
      case 4:
         return 45;
      case 5:
      case 6:
      case 7:
         return 40;
      case 8:
      case 9:
      case 10:
         return 35;
      case 11:
      case 12:
      case 13:
      case 14:
         return 30;
   }
   return 0;
}

function getMeso(itemid, extra) {
   var cost = getReq(itemid);
   if (cost < 5) {
      cost = 5;
   }
   return Math.floor(15000 * (extra + 1) + Math.pow(cost, 3) * Math.pow(extra + 1, 2) / 3);
}

function getAll(extra) {
   if (extra < 5) {
      return 2;
   } else if (extra < 10) {
      return 3;
   } else if (extra < 15) {
      return 5;
   }
   return 0;
}

function checkid(itemid) {
   switch (Math.floor(itemid / 10000)) {
      case 190:
      case 191:
         return false;
   }
   return true;
}

function getMapleList() {
   Inv = cm.getInventory(1);
   var checkInv = false;
   var message = "모험가님이 소지중인 일반아이템 목록입니다.\r\n";
   for (i = 0; i <= Inv.getSlotLimit(); i++) {
      if (Inv.getItem(i) == null) {
         continue;
      }
      if (cm.isCash(Inv.getItem(i).getItemId()) || !checkid(Inv.getItem(i).getItemId())) {
         continue;
      }
      checkInv = true;
      var geti = Inv.getItem(i);
      message += "#L" + i + "##i" + geti.getItemId() + ":# #b#z" + geti.getItemId() + "##k - " + geti.getExtraValue() + "강\r\n";
   }
   if (!checkInv) {
      cm.sendOk("아이템을 가지고 있는지 다시 한 번 확인해주세요.");
      cm.dispose();
      return;
   }
   cm.sendSimple(message);
}

function getMapleLv(itemid) {
   var lv = getReq(itemid);
   if (lv < 50) {
      return 5;
   } else if (lv < 100) {
      return 10;
   } else if (lv < 150) {
      return 15;
   } else if (lv < 200) {
      return 20;
   }
   return 5;
}

function getMapleMater(itemid) {
   var lv = getReq(itemid);
   if (lv < 50) {
      return 50;
   } else if (lv < 100) {
      return 200;
   } else if (lv < 150) {
      return 500;
   } else if (lv < 200) {
      return 1000;
   }
   return 1000;
}
