package com.ood.clean.waterball.a1a2bsdk.core;


import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.MockRoomListModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.MockUserSigningModule;

public class MockGameModuleFactory implements GameModuleFactory {

    @Override
    public GameModule createModule(ModuleName moduleName) {
        switch (moduleName)
        {
            case SIGNING:
                return new MockUserSigningModule();
            case ROOMLIST:
                return new MockRoomListModule();
            case CHAT:
                //return new ChatModuleImp();
            case INROOM:
                //return new InRoomModuleImp();
            case GAME1A2BDUEL:
                //return new Duel1A2BModuleImp();
            default:
                throw new IllegalArgumentException("Module name: " + moduleName + " not found.");
        }
    }
}
