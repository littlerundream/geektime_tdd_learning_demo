import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComponentConstructionTest {

    interface Car {
        public Engine getEngine();
    }

    interface Engine {
        public String getName();
    }

    static class V6Engine implements Engine {
        @Override
        public String getName() {
            return "V6";
        }
    }

    static class V8Engine implements Engine {
        @Override
        public String getName() {
            return "V8";
        }
    }

    @Nested
    public class ConstructorInjection {
        static class CarInjectConstructor implements Car {
            private Engine engine;
            @Inject
            public CarInjectConstructor(Engine engine) {
                this.engine = engine;
            }

            @Override
            public Engine getEngine() {
                return engine;
            }
        }
    }

    @Test
    public void should_constructor_injection_successfully() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Engine.class).to(V8Engine.class);
                bind(Car.class).to(ConstructorInjection.CarInjectConstructor.class);
            }
        });

        Car car = injector.getInstance(Car.class);
        assertEquals("V8", car.getEngine().getName());
    }
}