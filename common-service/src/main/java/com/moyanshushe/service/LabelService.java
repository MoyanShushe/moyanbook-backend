package com.moyanshushe.service;

import com.moyanshushe.model.dto.label.LabelForDelete;
import com.moyanshushe.model.dto.label.LabelForQuery;
import com.moyanshushe.model.dto.label.LabelInput;
import com.moyanshushe.model.dto.label.LabelSubstance;
import org.babyfish.jimmer.Page;

/*
 * Author: Hacoj
 * Version: 1.0
 */
public interface LabelService {
    Boolean addLabel(LabelInput label);

    Page<LabelSubstance> queryLabel(LabelForQuery labelForQuery);

    Boolean updateLabel(LabelInput label);

    Boolean deleteLabel(LabelForDelete label);
}
