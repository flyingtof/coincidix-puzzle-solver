package org.puzzle

import org.assertj.core.api.Assertions
import org.junit.Test

class FrameTest {
    @Test
    fun `are adjacent (same x or y)`() {
        Assertions.assertThat(Frame(0, 0).isAdjacentWith(Frame(0, 1))).isTrue()
        Assertions.assertThat(Frame(0, 0).isAdjacentWith(Frame(1, 0))).isTrue()
    }

    @Test
    fun `are not adjacent`() {
        Assertions.assertThat(Frame(0, 0).isAdjacentWith(Frame(1, 1))).isFalse()
    }

    @Test
    fun `are not adjacent (holes)`() {
        Assertions.assertThat(Frame(0, 0).isAdjacentWith(Frame(0, 2))).isFalse()
        Assertions.assertThat(Frame(0, 0).isAdjacentWith(Frame(2, 0))).isFalse()
    }
}
