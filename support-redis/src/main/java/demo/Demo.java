package demo;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.config.Config;

public class Demo {

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://118.24.38.46:6379")
                .setPassword("521428Slyt");
        Redisson redisson = (Redisson) Redisson.create(config);
        RLock rLock = redisson.getLock("1178515826");
        rLock.lock();
        System.out.println(rLock.isLocked());
        rLock.unlock();
        System.out.println(rLock.isLocked());
    }
}
