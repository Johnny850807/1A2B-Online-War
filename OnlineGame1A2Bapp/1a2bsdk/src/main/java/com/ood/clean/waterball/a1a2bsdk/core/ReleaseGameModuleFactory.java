package com.ood.clean.waterball.a1a2bsdk.core;

import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.ChatModuleImp;
import com.ood.clean.waterball.a1a2bsdk.core.modules.games.a1b2.duel.Duel1A2BModuleImp;
import com.ood.clean.waterball.a1a2bsdk.core.modules.inRoom.InRoomModuleImp;
import com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.RoomListModuleImp;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModuleImp;



public class ReleaseGameModuleFactory implements GameModuleFactory {

    @Override
    public GameModule createModule(ModuleName moduleName) {
        switch (moduleName)
        {
            case SIGNING:
                return new UserSigningModuleImp();
            case ROOMLIST:
                return new RoomListModuleImp();
            case CHAT:
                return new ChatModuleImp();
            case INROOM:
                return new InRoomModuleImp();
            case GAME1A2BDUEL:
                return new Duel1A2BModuleImp();
            default:
                throw new IllegalArgumentException("Module name: " + moduleName + " not found.");
        }
    }
}
