/**
 * Copyright 2013 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rx.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.*;

import static org.junit.Assert.assertEquals;

public final class Range implements Iterable<Integer> {
    private final int start;
    private final int end;
    private final int step;

    public static Range createWithCount(int start, int count) {
        return create(start, start + count);
    }

    public static Range create(int start, int end) {
        return new Range(start, end, 1);
    }

    public static Range createWithStep(int start, int end, int step) {
        return new Range(start, end, step);
    }

    private Range(int start, int end, int step) {
        this.start = start;
        this.end = end;
        this.step = step;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private int current = start;

            @Override
            public boolean hasNext() {
                return current < end;
            }

            @Override
            public Integer next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements");
                }
                int result = current;
                current += step;
                return result;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Read only iterator");
            }
        };
    }

    @Override
    public String toString() {
        return "Range (" + start + ", " + end + "), step " + step;
    }


    public static class UnitTest {

        @Test
        public void testSimpleRange() {
            assertEquals(Arrays.asList(1, 2, 3, 4), toList(Range.create(1, 5)));
        }

        @Test
        public void testRangeWithStep() {
            assertEquals(Arrays.asList(1, 3, 5, 7, 9), toList(Range.createWithStep(1, 10, 2)));
        }

        @Test
        public void testRangeWithCount() {
            assertEquals(Arrays.asList(1, 2, 3, 4, 5), toList(Range.createWithCount(1, 5)));
        }


        private static <T> List<T> toList(Iterable<T> iterable) {
            List<T> result = new ArrayList<T>();
            for (T element : iterable) {
                result.add(element);
            }
            return result;
        }

    }
	
    @RunWith(Parameterized.class)
    public static class ParameterizedCountUnitTest {
        
        private int start;
        private int count;
        private List<Integer> expected;
        
        public ParameterizedCountUnitTest (int start, int count, List<Integer> expected) {
            this.start = start;
            this.count = count;
            this.expected = expected;
        }
        
        @Parameters
        public static List<?> generateData() {
            return Arrays.asList(new Object[][] {
                    {1, 1, Arrays.asList(1)},
                    {2, 4, Arrays.asList(2, 3, 4, 5)},
                    {4, 1, Arrays.asList(4)},
                    {0, 1, Arrays.asList(0)},
                    {-2, 2, Arrays.asList(-2, -1)},
                    {0, -1, Collections.EMPTY_LIST}
            });
        }
        
        @Test
        public void testRangeWithCount() {
           assertEquals(this.expected, UnitTest.toList(Range.createWithCount(this.start, this.count)));
        }
    } 	
}