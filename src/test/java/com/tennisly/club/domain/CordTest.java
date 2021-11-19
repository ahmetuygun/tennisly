package com.tennisly.club.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tennisly.club.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cord.class);
        Cord cord1 = new Cord();
        cord1.setId(1L);
        Cord cord2 = new Cord();
        cord2.setId(cord1.getId());
        assertThat(cord1).isEqualTo(cord2);
        cord2.setId(2L);
        assertThat(cord1).isNotEqualTo(cord2);
        cord1.setId(null);
        assertThat(cord1).isNotEqualTo(cord2);
    }
}
