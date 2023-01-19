package com.koreait.projectboard.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaginationService {

    private static final int BAR_LENGTH = 5;

    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages){
        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH / 2), 0); //만약 음수가 나오면 0으로 나온다.
        int endNumber = Math.min(startNumber + BAR_LENGTH, totalPages);
        return IntStream.range(startNumber, endNumber).boxed().toList();
        //만약 (1,11)이라고 쓰면 알아서 이 범위의 숫자를 만들어준다. -- Integer 형으로
        //박싱! - 래퍼클래스로 변경! 그리고 List로 만들어준다.
    }

    public int currentBarLength(){
        return BAR_LENGTH;
    }
}
