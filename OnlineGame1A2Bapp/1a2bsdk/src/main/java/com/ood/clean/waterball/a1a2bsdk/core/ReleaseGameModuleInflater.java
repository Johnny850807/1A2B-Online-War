package com.ood.clean.waterball.a1a2bsdk.core;

import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.ChatModuleImp;
import com.ood.clean.waterball.a1a2bsdk.core.modules.games.Duel1A2BModuleImp;
import com.ood.clean.waterball.a1a2bsdk.core.modules.inRoom.InRoomModuleImp;
import com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.RoomListModuleImp;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModuleImp;

import java.util.Map;



public class ReleaseGameModuleInflater implements GameModuleInflater {

    @Override
    public void onPrepareModules(Map<ModuleName, GameModule> moduleMap) {
        moduleMap.put(ModuleName.SIGNING, new UserSigningModuleImp());
        moduleMap.put(ModuleName.ROOMLIST, new RoomListModuleImp());
        moduleMap.put(ModuleName.CHAT, new ChatModuleImp());
        moduleMap.put(ModuleName.INROOM, new InRoomModuleImp());
        moduleMap.put(ModuleName.GAME1A2BDUEL, new Duel1A2BModuleImp());
    }

}
