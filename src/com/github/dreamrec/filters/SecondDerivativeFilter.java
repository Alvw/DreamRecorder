package com.github.dreamrec.filters;

import com.github.dreamrec.filters.AbstractFilter;
import com.github.dreamrec.filters.Filter;

/**
 *
 */
public class SecondDerivativeFilter extends AbstractFilter<Integer> {

    public SecondDerivativeFilter(Filter<Integer> inputData) {
        super(inputData);
    }

    @Override
    protected Integer doFilter(int index) {
        if (index < 8) {
            return 0;
        }
        int sum = inputData.get(index - 8) +
                inputData.get(index - 7) * 2 +
                inputData.get(index - 6) * 4 +
                inputData.get(index - 5) * 8 +
                inputData.get(index - 3) * 8 +
                inputData.get(index - 2) * 4 +
                inputData.get(index - 1) * 2 +
                inputData.get(index);
        return (inputData.get(index - 4) - sum / 30);
    }
}