package com.ood.clean.waterball.a1a2bsdk.core;


import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.MockUserSigningModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.MockRoomListModule;

import java.util.Map;

public class MockGameModuleInflater implements GameModuleInflater{

    @Override
    public void onPrepareModules(Map<ModuleName, GameModule> moduleMap) {
        moduleMap.put(ModuleName.SIGNING, new MockUserSigningModule());
        moduleMap.put(ModuleName.ROOMLIST, new MockRoomListModule());
        //moduleMap.put(ModuleName.GAME1A2BDUEL, new MockDuel1A2BGameModule());
    }

}
