/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package by.d1makrat.library_fm;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "versionApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "library_fm.d1makrat.by",
                ownerName = "library_fm.d1makrat.by",
                packagePath = ""
        )
)
public class VersionEndpoint {

    @ApiMethod(name = "getLatestVersion",
            path = "version",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Version getLatestVersion() {

        return new Version();
    }
}
