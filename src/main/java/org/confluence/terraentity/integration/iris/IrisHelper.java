package org.confluence.terraentity.integration.iris;

import com.mojang.blaze3d.systems.RenderSystem;
import net.irisshaders.iris.pipeline.programs.ExtendedShader;
import org.confluence.terraentity.client.event.RenderEvent;
import org.confluence.terraentity.integration.ModChecker;

public class IrisHelper {

    // client only
    public static boolean isIrisShader(){
        return RenderEvent.isIrisShader;
    }


}
