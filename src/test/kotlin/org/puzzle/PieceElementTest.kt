package org.puzzle

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PieceElementTest {
    @Test
    fun `are adjacent (same x or y)`() {
        assertThat(PieceElement(0, 0).isAdjacentWith(PieceElement(0, 1))).isTrue()
        assertThat(PieceElement(0, 0).isAdjacentWith(PieceElement(1, 0))).isTrue()
    }

    @Test
    fun `are not adjacent`() {
        assertThat(PieceElement(0, 0).isAdjacentWith(PieceElement(1, 1))).isFalse()
    }

    @Test
    fun `are not adjacent (holes)`() {
        assertThat(PieceElement(0, 0).isAdjacentWith(PieceElement(0, 2))).isFalse()
        assertThat(PieceElement(0, 0).isAdjacentWith(PieceElement(2, 0))).isFalse()
    }
}
