package org.celllife.stockout.app.domain.comparator;

import java.util.Comparator;

import org.celllife.stockout.app.domain.Drug;

public class DrugComparator implements Comparator<Drug> {

    @Override
    public int compare(Drug lhs, Drug rhs) {
        if (lhs == null && rhs == null) {
            return 0;
        }
        if (lhs == null) {
            return 1;
        }
        if (rhs == null) {
            return -1;
        }
        String drug1 = lhs.getDescription();
        String drug2 = rhs.getDescription();
        return drug1.compareTo(drug2);
    }

}
