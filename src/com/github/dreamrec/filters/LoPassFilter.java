package com.github.dreamrec.filters;

import com.github.dreamrec.filters.AbstractFilter;
import com.github.dreamrec.filters.Filter;

/**
 *
 */
public class LoPassFilter extends AbstractFilter<Integer> {

    private int bufferSize;

    public LoPassFilter(int bufferSize, Filter<Integer> inputData) {
        super(inputData);
        this.bufferSize = bufferSize;
    }

    @Override
    protected Integer doFilter(int index) {
        int sum = 0;
        if(index<bufferSize-1){
            return inputData.get(index);
        }else {
            for (int i = 0; i < bufferSize; i++) {
                 sum+=inputData.get(index - i);
            }
            return (sum/bufferSize);
        }
    }
}
