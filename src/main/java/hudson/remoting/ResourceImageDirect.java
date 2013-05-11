package hudson.remoting;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static hudson.remoting.Util.*;

/**
 * {@link ResourceImageRef} that directly encapsulates the resource as {@code byte[]}.
 *
 * <p>
 * This is used when {@link ResourceImageInJar} cannot be used because we couldn't identify the jar file.
 *
 * @author Kohsuke Kawaguchi
 */
class ResourceImageDirect extends ResourceImageRef {
    /**
     * The actual resource.
     */
    private final byte[] payload;

    ResourceImageDirect(byte[] payload) {
        this.payload = payload;
    }

    ResourceImageDirect(URL resource) throws IOException {
        this(Util.readFully(resource.openStream()));
    }

    @Override
    byte[] resolve(Channel channel, String resourcePath) throws IOException {
        LOGGER.log(Level.FINE, resourcePath+" image is direct");
        return payload;
    }

    @Override
    URLish resolveURL(Channel channel, String resourcePath) throws IOException {
        return URLish.from(makeResource(getBaseName(resourcePath), payload));
    }

    private static final Logger LOGGER = Logger.getLogger(ResourceImageDirect.class.getName());

    private static final long serialVersionUID = 1L;
}
