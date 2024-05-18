import LunitLogo from '@assets/LunitLogo.png';
import { AuthAtom } from '@global/AuthAtom';
import { IconButton } from '@mui/material';
import { Tag } from '@pages/main/tag';
import { TagsInputComponent } from '@pages/main/tagInput';
import React, { useEffect, useRef, useState } from 'react';
import { createPortal } from 'react-dom';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { createSdkKey, getMainPageOverview } from '@/api/main/mainAxios';
import CopyCheck from '@/assets/check.svg?react';
import CopyButton from '@/assets/content-copy.svg?react';
import FilteringIcon from '@/assets/filtering.svg?react';
import FilledFlag from '@/assets/flag.svg?react';
import OutlinedFlag from '@/assets/outlined-flag.svg?react';
import SdkKey from '@/assets/sdk-key.svg?react';
import SearchIcon from '@/assets/search.svg?react';
import CreateModal from '@/components/createModal';
import * as S from '@/pages/main/indexStyle';
import FlagTable from '@/pages/main/table';
import { useLoadingStore } from '@/global/LoadingAtom';
interface OverviewInfo {
  sdkKey: string;
  totalFlags: number;
  activeFlags: number;
}

interface SdkKeyResDto {
  key: string;
}

const index = () => {
  const [sdkKey, setSdkKey] = useState<string>('');
  const [totalFlags, setTotalFlags] = useState<number>(0);
  const [activeFlags, setActiveFlags] = useState<number>(0);
  const [isModalOpened, setIsModalOpened] = useState(false);
  const [flagKeyword, setFlagKeyword] = useState<string>('');
  const [nullSDK, setNullSDK] = useState<boolean>(false);
  const [isDropdownOpened, setIsDropdownOpened] = useState(false);
  const auth = useRecoilValue(AuthAtom);
  const dropdownContainerRef = useRef(null);
  const [selectedTags, setSelectedTags] = useState<Array<Tag>>([]);
  const [isCopied, setIsCopied] = useState<boolean>(false);
  const navigate = useNavigate();
  const { loading, contentLoading, contentLoaded } = useLoadingStore();
  const copyToClipboard = async () => {
    try {
      await navigator.clipboard.writeText(sdkKey);
      setIsCopied(true);
      setTimeout(() => setIsCopied(false), 2000);
    } catch (err) {
      console.log(err);
      navigate('/');
    }
  };

  useEffect(() => {
    console.log('auth');
    console.log(auth);
    if (sdkKey == '') {
      setNullSDK(true);
    } else {
      setNullSDK(false);
    }
  }, [auth, sdkKey]);

  useEffect(() => {
    let vh = 0;
    vh = window.innerHeight * 0.01;
    document.documentElement.style.setProperty('--vh', `${vh}px`);
  }, [window.innerHeight]);

  /**
   * 화면 마운트 시 필요한 정보 가져오기
   */
  useEffect(() => {
    contentLoading();
    getMainPageOverview(
      (data: OverviewInfo) => {
        console.log('data');
        console.log(auth);
        setSdkKey(data.sdkKey ? data.sdkKey : '');
        setTotalFlags(data.totalFlags);
        setActiveFlags(data.activeFlags);
        contentLoaded();
      },
      (err) => {
        console.error(err);
        contentLoaded();
      },
    );
  }, [auth]);

  // sdk 발급 받으면 sdk component 갱신
  useEffect(() => {
    console.log(sdkKey);
  }, [sdkKey]);

  useEffect(() => {
    let vh = 0;
    vh = window.innerHeight * 0.01;
    document.documentElement.style.setProperty('--vh', `${vh}px`);
  }, [window.innerHeight]);

  const html = document.querySelector('html');
  const openCreateModal = () => {
    setIsModalOpened(true);
    html?.classList.add('scroll-locked');
  };

  const closeCreateModal = () => {
    const closeConfirm = window.confirm('진짜 닫을거야?');
    if (closeConfirm) {
      setIsModalOpened(false);
      html?.classList.remove('scroll-locked');
    }
  };

  const openDropdown = () => {
    setIsDropdownOpened(true);
    if (isDropdownOpened) {
      setIsDropdownOpened(false);
    }
  };
  const handleFlagSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFlagKeyword(e.target.value);
  };

  const handleClickCreateSdkKeyButton = () => {
    console.log('sdk key 발급');
    console.log(auth.email);
    createSdkKey<SdkKeyResDto>(
      { email: auth.email },
      (data: SdkKeyResDto) => {
        console.log(data);
        setSdkKey(data.key ? data.key : '');
      },
      (err) => {
        console.error(err);
      },
    );
  };

  return (
    <S.MainLayout>
      {isModalOpened &&
        createPortal(
          <CreateModal
            closeCreateModal={closeCreateModal}
            mode={'create'}
            flagDetail={undefined}
          />,
          document.body,
        )}
      <S.ComponentContainer>
        <S.MainTitleComponent>
          <S.imageContainer>
            <S.imageLunitLogo path={LunitLogo} />
          </S.imageContainer>
          <S.LunitInfoContainer>
            <S.LunitTitle>Light Switch</S.LunitTitle>
            <S.InfoText>
              Light Switch는 플래그를 관리하고, 플래그를 통해 서비스의 기능을 제어할 수
              있는 플랫폼입니다.
            </S.InfoText>
          </S.LunitInfoContainer>
        </S.MainTitleComponent>

        <S.OverviewComponent>
          <S.SdkKeyComponent>
            <S.SdkKeyTitleContainer>
              <S.Title>SDK 키</S.Title>
              {!nullSDK && (
                <>
                  {!isCopied ? (
                    <IconButton
                      onClick={copyToClipboard}
                      style={{ cursor: 'pointer', width: '2.5rem', height: '2.5rem' }}
                    >
                      <CopyButton />
                    </IconButton>
                  ) : (
                    <IconButton
                      onClick={copyToClipboard}
                      style={{ cursor: 'pointer', width: '2.5rem', height: '2.5rem' }}
                    >
                      <CopyCheck style={{ width: '100%', height: '100%' }} />
                    </IconButton>
                  )}
                </>
              )}
            </S.SdkKeyTitleContainer>
            <S.SdkkeyContentContainer>
              <S.SdkKeyIconContainer>
                <SdkKey />
              </S.SdkKeyIconContainer>
              <S.SdkKeyTextContainer>
                {sdkKey.length > 0 ? (
                  <S.SdkKeyText>{sdkKey}</S.SdkKeyText>
                ) : (
                  <S.NoExistSdkKeyText>
                    <S.SdkKeyText>SDK 키가 없습니다.</S.SdkKeyText>
                    <S.createSdkKeyButton onClick={handleClickCreateSdkKeyButton}>
                      <S.SdkKeyText>SDK 키 발급</S.SdkKeyText>
                    </S.createSdkKeyButton>
                  </S.NoExistSdkKeyText>
                )}
              </S.SdkKeyTextContainer>
            </S.SdkkeyContentContainer>
          </S.SdkKeyComponent>

          <S.FlagComponent>
            <S.FlagTitleContainer>
              <S.Title>관리 플래그</S.Title>
            </S.FlagTitleContainer>
            <S.FlagContentContainer>
              <S.FlagCountContainer>
                <S.FlagCountIconContainer>
                  <OutlinedFlag />
                </S.FlagCountIconContainer>
                <S.FlagCountTextContainer>
                  <S.FlagCountTextSmallContainer>
                    <S.FlagCountTextSmall>총</S.FlagCountTextSmall>
                  </S.FlagCountTextSmallContainer>
                  <S.FlagCountTextBox>
                    <S.FlagCountTextBigContainer>
                      <S.FlagCountTextBig>{totalFlags}</S.FlagCountTextBig>
                      {/* <S.FlagCountTextBig>13</S.FlagCountTextBig> */}
                    </S.FlagCountTextBigContainer>
                    <S.FlagCountTextSmallContainer>
                      <S.FlagCountTextSmall>개</S.FlagCountTextSmall>
                    </S.FlagCountTextSmallContainer>
                  </S.FlagCountTextBox>
                </S.FlagCountTextContainer>
              </S.FlagCountContainer>

              <S.FlagCountContainer>
                <S.FlagCountIconContainer>
                  <FilledFlag />
                </S.FlagCountIconContainer>
                <S.FlagCountTextContainer>
                  <S.FlagCountTextSmallContainer>
                    <S.FlagCountTextSmall>활성</S.FlagCountTextSmall>
                  </S.FlagCountTextSmallContainer>
                  <S.FlagCountTextBox>
                    <S.FlagCountTextBigContainer>
                      <S.FlagCountTextBig>{activeFlags}</S.FlagCountTextBig>
                      {/* <S.FlagCountTextBig>7</S.FlagCountTextBig> */}
                    </S.FlagCountTextBigContainer>
                    <S.FlagCountTextSmallContainer>
                      <S.FlagCountTextSmall>개</S.FlagCountTextSmall>
                    </S.FlagCountTextSmallContainer>
                  </S.FlagCountTextBox>
                </S.FlagCountTextContainer>
              </S.FlagCountContainer>
            </S.FlagContentContainer>
          </S.FlagComponent>

          <S.HistoryComponent>
            <S.HisotryTitleContainer>
              <S.Title>히스토리</S.Title>
            </S.HisotryTitleContainer>
          </S.HistoryComponent>
        </S.OverviewComponent>

        <S.FlagTableComponent>
          <S.TableNavContainer>
            <S.FlagNavTitleContainer>
              <S.FlagNavTitleContainer>
                <S.Title>플래그</S.Title>
              </S.FlagNavTitleContainer>
              <S.FlagNavSearchComponent ref={dropdownContainerRef}>
                <S.FlagNavSearchBoxContainer>
                  <S.FlagNavSearchInput placeholder="검색" onChange={handleFlagSearch} />
                  <S.SearchIconContainer>
                    <SearchIcon />
                  </S.SearchIconContainer>
                </S.FlagNavSearchBoxContainer>

                <S.FlagNavFilteringContainer>
                  <S.FlagNavFilteringButton onClick={() => openDropdown()}>
                    <S.FlagNavFiltering>
                      <FilteringIcon />
                    </S.FlagNavFiltering>
                  </S.FlagNavFilteringButton>
                </S.FlagNavFilteringContainer>
                {isDropdownOpened &&
                  dropdownContainerRef.current &&
                  createPortal(
                    <TagsInputComponent
                      selectedTags={selectedTags}
                      setSelectedTags={setSelectedTags}
                      allowCreation={false}
                    />,
                    dropdownContainerRef.current,
                  )}
              </S.FlagNavSearchComponent>
            </S.FlagNavTitleContainer>

            <S.FlagNavCreateButtonContainer>
              <S.FlagNavCreateButton>
                <S.ButtonText onClick={() => openCreateModal()}>
                  플래그 만들기
                </S.ButtonText>
              </S.FlagNavCreateButton>
            </S.FlagNavCreateButtonContainer>
          </S.TableNavContainer>

          <S.FlagTableContainer>
            <FlagTable
              flagKeyword={flagKeyword}
              tags={selectedTags}
              activeFlagChanged={(acvtiveFlags) => setActiveFlags(acvtiveFlags)}
            />
          </S.FlagTableContainer>
        </S.FlagTableComponent>
      </S.ComponentContainer>
    </S.MainLayout>
  );
};

export default index;
