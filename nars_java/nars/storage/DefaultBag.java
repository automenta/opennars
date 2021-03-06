/*
 * Copyright (C) 2014 me
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package nars.storage;

import java.util.concurrent.atomic.AtomicInteger;
import nars.entity.Item;

/**
 *
 * @author me
 */
public class DefaultBag<E extends Item> extends Bag<E> {
    private final AtomicInteger forgetRate;

    public DefaultBag(int levels, int capacity, int forgetRate) {
        this(levels, capacity, new AtomicInteger(forgetRate));        
    }
    
    public DefaultBag(int levels, int capacity, AtomicInteger forgetRate) {
        super(levels, capacity);
        this.forgetRate = forgetRate;
    }


    @Override
    protected int forgetRate() {
        return forgetRate.get();
    }
    
}
